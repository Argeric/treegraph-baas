package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_ROOT_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_PKI_PATH_ROOT_CA;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_ROLE_CONS_CA_ISSUER;

import com.alibaba.fastjson.JSON;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.support.VaultCertificateRequest;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultSignCertificateRequestResponse;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.service.VaultService;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

  private final VaultOperations vaultOperations;

  public boolean mount(String pkiPath, int ttlInHour) {
    VaultSysOperations adminOperations = vaultOperations.opsForSys();
    if (!adminOperations.getMounts().containsKey(pkiPath + "/")) {
      VaultMount mount = VaultMount.builder().type("pki")
          .config(Collections.singletonMap("max_lease_ttl", ttlInHour + "h")).description("bass pki")
          .build();
      adminOperations.mount(pkiPath, mount);

      Map<String, VaultMount> mounts = adminOperations.getMounts();
      log.debug("mount pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(mounts));
      Assert.isTrue(mounts.containsKey(pkiPath + "/"), "vault mount pki path error");
    }
    return true;
  }

  public void createRole(String pkiPath, String roleName, int ttlInHour) {
    Map<String, String> body = new HashMap<>();
    body.put("allowed_domains", "localhost,baas.conflux-chain.org");
    body.put("allow_subdomains", "true");
    body.put("allow_localhost", "true");
    body.put("allow_ip_sans", "true");
    body.put("max_ttl", ttlInHour + "h");
    VaultResponse createRoleResponse = vaultOperations.write(
        pkiPath + "/roles/" + roleName, body);
    log.debug("createRole pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(createRoleResponse));
  }

  public VaultGenerateRootBo generateRoot() {
    boolean mountFlag = mount(VAULT_PKI_PATH_ROOT_CA, TTL_ROOT_CA_IN_DAY);
    if(mountFlag) return VaultGenerateRootBo.builder().build();

    Map<String, String> displayNameMap = CertUtil.parseSubject(KeyType.ROOT_CA.getSubject().getCode());
    Map<String, Object> body = new HashMap<>();
    body.put("common_name", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN)));
    body.put("ou", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.OU)));
    body.put("organization", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.O)));
    body.put("locality", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.L)));
    body.put("province", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.ST)));
    body.put("country", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.C)));
    body.put("ttl", TTL_ROOT_CA_IN_DAY * 24);
    body.put("format", "pem");
    body.put("private_key_format", "der");
    body.put("key_type", "rsa");
    body.put("key_bits", 2048);
    VaultResponse generateRootResponse = vaultOperations
        .write(VAULT_PKI_PATH_ROOT_CA + "/root/generate/exported", body);
    Assert.notNull(generateRootResponse, "gene.root.ca.not.response");

    createRole(VAULT_PKI_PATH_ROOT_CA, VAULT_ROLE_CONS_CA_ISSUER, TTL_ROOT_CA_IN_DAY);

    Map<String, Object> dataMap = generateRootResponse.getData();
    Assert.notNull(dataMap, "generateRoot.dataMap.is.null");
    return VaultGenerateRootBo.builder().certificate((String) dataMap.get("certificate"))
        .privateKey((String) dataMap.get("private_key"))
        .serialNumber((String) dataMap.get("serial_number"))
        .build();
  }

  public void submitCAInformation(String pkiPath, String certPem, String keyPem, int ttlInDay) {
    int ttlInHour = ttlInDay * 24;
    mount(pkiPath, ttlInHour);

    Map<String, String> pemBundle = Collections.singletonMap("pem_bundle", certPem + keyPem);
    VaultResponse writeCaToVaultResponse = vaultOperations.write(pkiPath + "/config/ca",
        pemBundle);
    log.debug("writeCaResponse pkiPath:{},resp:{}", pkiPath,
        JSON.toJSONString(writeCaToVaultResponse));

    Map<String, String> role = new HashMap<>();
    role.put("allowed_domains", "localhost,baas.conflux-chain.org");
    role.put("allow_subdomains", "true");
    role.put("allow_localhost", "true");
    role.put("allow_ip_sans", "true");
    role.put("max_ttl", ttlInHour + "h");
    VaultResponse writeRoleToVaultResponse = vaultOperations.write(
        pkiPath + "/roles/" + X509CertConstant.VAULT_ROLE_RPC_CERT_ISSUER, role);
    log.debug("writeRoleResponse pkiPath:{},resp:{}", pkiPath,
        JSON.toJSONString(writeRoleToVaultResponse));
  }

  public VaultSignIntermediateBo signIntermediate(String pkiPath, String csr){
    Map<String, String> displayNameMap = CertUtil.parseSubject(KeyType.CONSORTIUM.getSubject().getCode());
    String commonName = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));

    Map<String, String> body = new HashMap<>();
    body.put("csr", csr);
    body.put("common_name", commonName);
    body.put("format", "pem");
    body.put("use_csr_values", "true");
    body.put("max_ttl", (TTL_CONSORTIUM_CA_IN_DAY * 24) + "h");
    VaultResponse signIntermediateResponse = vaultOperations.write(
        pkiPath + "/root/sign-intermediate", body);
    Assert.notNull(signIntermediateResponse, "signIntermediate.resp.is.null");
    log.debug("signIntermediate pkiPath:{},resp:{}", pkiPath,
        JSON.toJSONString(signIntermediateResponse));

    Map<String, Object> dataMap = signIntermediateResponse.getData();
    Assert.notNull(dataMap, "signIntermediate.dataMap.is.null");
    return VaultSignIntermediateBo.builder().certificate((String) dataMap.get("certificate"))
        .serialNumber((String) dataMap.get("serial_number")).build();
  }

  public String signCertificate(String pkiPath, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl, String issuer) throws Exception {
    int lastCertTtlInDay = CertUtil.getLastCrtTtlInDay(lastCrtExpire);
    Map<String, String> displayNameMap = CertUtil.parseSubject(csr.getSubject());
    String cn = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));

    VaultCertificateRequest request = VaultCertificateRequest.builder()
        .ttl(Duration.ofDays(lastCertTtlInDay + ttl))
        .commonName(cn).build();
    VaultSignCertificateRequestResponse certificateResponse = vaultOperations
        .opsForPki(pkiPath)
        .signCertificateRequest(issuer, CertUtil.csrToPem(csr), request);
    org.springframework.vault.support.Certificate cert = certificateResponse.getRequiredData();
    return CertUtil.certBase64ToPem(cert.getCertificate());
  }
}
