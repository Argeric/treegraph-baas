package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_ROOT_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_PKI_PATH_ROOT_CA;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_ROLE_CONS_CA_ISSUER;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_ROLE_RPC_CERT_ISSUER;

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
import org.treegraph.baas.core.service.bo.VaultGenerateIntermediateBo;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

  private final VaultOperations vaultOperations;

  public void mount(String pkiPath, int ttlInDay) {
    VaultSysOperations adminOperations = vaultOperations.opsForSys();
    Map<String, VaultMount> mounts = adminOperations.getMounts();
    Assert.isTrue(!mounts.containsKey(pkiPath + "/"), pkiPath + ".mount.already");

    VaultMount mount = VaultMount.builder().type("pki")
        .config(Collections.singletonMap("max_lease_ttl", (ttlInDay * 24) + "h")).description("bass pki")
        .build();
    adminOperations.mount(pkiPath, mount);

    mounts = adminOperations.getMounts();
    log.debug("mount pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(mounts));
    Assert.isTrue(mounts.containsKey(pkiPath + "/"), "vault mount pki path error");
  }

  public void createRole(String pkiPath, String roleName, int ttlInDay) {
    Map<String, String> body = new HashMap<>();
    body.put("allowed_domains", "localhost,baas.conflux-chain.org");
    body.put("allow_subdomains", "true");
    body.put("allow_localhost", "true");
    body.put("allow_ip_sans", "true");
    body.put("max_ttl",  (ttlInDay * 24) + "h");
    VaultResponse createRoleResponse = vaultOperations.write(
        pkiPath + "/roles/" + roleName, body);
    log.debug("createRole pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(createRoleResponse));
  }

  public VaultGenerateRootBo generateRoot() {
    mount(VAULT_PKI_PATH_ROOT_CA, TTL_ROOT_CA_IN_DAY);

    Map<String, String> displayNameMap = CertUtil.parseSubject(KeyType.ROOT_CA.getSubject().getCode());
    Map<String, Object> body = new HashMap<>();
    body.put("common_name", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN)));
    body.put("ou", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.OU)));
    body.put("organization", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.O)));
    body.put("locality", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.L)));
    body.put("province", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.ST)));
    body.put("country", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.C)));
    body.put("ttl", (TTL_ROOT_CA_IN_DAY * 24)+ "h");
    body.put("format", "pem");
    body.put("private_key_format", "der");
    body.put("key_type", "rsa");
    body.put("key_bits", 2048);
    VaultResponse generateRootResponse = vaultOperations
        .write("/" + VAULT_PKI_PATH_ROOT_CA + "/root/generate/exported", body);
    Assert.notNull(generateRootResponse, "generateRoot.no.response");

    createRole(VAULT_PKI_PATH_ROOT_CA, VAULT_ROLE_CONS_CA_ISSUER, TTL_ROOT_CA_IN_DAY);

    Map<String, Object> dataMap = generateRootResponse.getData();
    Assert.notNull(dataMap, "generateRoot.dataMap.is.null");
    String privateKey = KeyUtil.pemToBase64((String) dataMap.get("private_key"));
    return VaultGenerateRootBo.builder().certificate((String) dataMap.get("certificate"))
        .privateKey(privateKey)
        .serialNumber((String) dataMap.get("serial_number"))
        .build();
  }

  public void submitCAInformation(String pkiPath, String certPem, String keyPem, int ttlInDay) {
    mount(pkiPath, ttlInDay);
    Map<String, String> pemBundle = Collections.singletonMap("pem_bundle", certPem + keyPem);
    VaultResponse submitCAInfoResp = vaultOperations.write(pkiPath + "/config/ca", pemBundle);
    log.debug("writeCaResponse pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(submitCAInfoResp));
    createRole(pkiPath, X509CertConstant.VAULT_ROLE_RPC_CERT_ISSUER, ttlInDay);
  }

  public VaultGenerateIntermediateBo generateIntermediate(String consPkiPath){
    mount(consPkiPath, TTL_CONSORTIUM_CA_IN_DAY);
    Map<String, String> displayNameMap = CertUtil.parseSubject(KeyType.CONSORTIUM.getSubject().getCode());
    Map<String, Object> body = new HashMap<>();
    body.put("common_name", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN)));
    body.put("ou", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.OU)));
    body.put("organization", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.O)));
    body.put("locality", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.L)));
    body.put("province", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.ST)));
    body.put("country", displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.C)));
    body.put("format", "pem");
    body.put("private_key_format", "der");
    body.put("key_type", "rsa");
    body.put("key_bits", 2048);
    VaultResponse generateIntermediateResponse = vaultOperations
        .write(consPkiPath + "/intermediate/generate/exported", body);
    Assert.notNull(generateIntermediateResponse, "generateIntermediate.ca.not.response");

    Map<String, Object> dataMap = generateIntermediateResponse.getData();
    Assert.notNull(dataMap, "generateIntermediate.dataMap.is.null");
    String privateKey = KeyUtil.pemToBase64((String) dataMap.get("private_key"));
    return VaultGenerateIntermediateBo.builder().csr((String) dataMap.get("csr"))
        .privateKey(privateKey)
        .serialNumber((String) dataMap.get("serial_number"))
        .build();
  }

  public VaultSignIntermediateBo signIntermediate(String csr){
    Map<String, String> displayNameMap = CertUtil.parseSubject(KeyType.CONSORTIUM.getSubject().getCode());
    String commonName = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));

    Map<String, String> body = new HashMap<>();
    body.put("csr", csr);
    body.put("common_name", commonName);
    body.put("format", "pem");
    body.put("use_csr_values", "true");
    body.put("ttl", (TTL_CONSORTIUM_CA_IN_DAY * 24) + "h");
    VaultResponse signIntermediateResponse = vaultOperations.write(
        VAULT_PKI_PATH_ROOT_CA + "/root/sign-intermediate", body);
    Assert.notNull(signIntermediateResponse, "signIntermediate.resp.is.null");

    Map<String, Object> dataMap = signIntermediateResponse.getData();
    Assert.notNull(dataMap, "signIntermediate.dataMap.is.null");
    return VaultSignIntermediateBo.builder().certificate((String) dataMap.get("certificate"))
        .serialNumber((String) dataMap.get("serial_number")).build();
  }

  public void setSignedIntermediate(String consPkiPath, String certPem){
    Map<String, String> body = new HashMap<>();
    body.put("certificate", certPem);
    VaultResponse setSignedIntermediateResponse = vaultOperations.write(
        consPkiPath + "/intermediate/set-signed", body);
    log.debug("setSignedIntermediateResponse pkiPath:{},resp:{}", consPkiPath,
        JSON.toJSONString(setSignedIntermediateResponse));

    createRole(consPkiPath, VAULT_ROLE_RPC_CERT_ISSUER, TTL_CONSORTIUM_CA_IN_DAY);
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
