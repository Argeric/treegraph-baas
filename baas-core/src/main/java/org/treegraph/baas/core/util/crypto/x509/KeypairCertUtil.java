package org.treegraph.baas.core.util.crypto.x509;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_ROOT_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_RPC_CERT_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_PKI_PATH_ROOT_CA;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Certificate;
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
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.CertSignType;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.service.bo.VaultDataGeneRootBo;
import org.treegraph.baas.core.util.DateUtils;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {

  private final VaultOperations vaultOperations;

  public KeypairCertBo buildRootKeypairCert() {
    return this.buildKeypairCert(KeyType.ROOT_CA, null, null, null, null);
  }

  public KeypairCertBo buildConsKeypairCert(Date lastCrtExpire, KeypairPo bcRootCa) {
    return this.buildKeypairCert(KeyType.CONSORTIUM, lastCrtExpire, null, null, bcRootCa);
  }

  public KeypairCertBo buildNodeKeypairCert() {
    return this.buildKeypairCert(KeyType.NODE, null, null, null, null);
  }

  public KeypairCertBo buildRpcKeypairCert(Date lastCrtExpire, List<String> sanList, String vaultCa,
      KeypairPo bcRootCa) {
    return this.buildKeypairCert(KeyType.RPC, lastCrtExpire, sanList, vaultCa, bcRootCa);
  }

  private KeypairCertBo buildKeypairCert(CryptoEnum.KeyType keyType, Date lastCrtExpire,
      List<String> sanList, String vaultCa, KeypairPo bcCa) {
    KeyPair keyPair;
    String publicKey;
    String privateKey;
    int keyTypeCode = keyType.getCode();
    try {
      keyPair = KeyUtil.generateKeyPair(keyType.getAlgo().getCode());
      publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
      privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
    } catch (Exception e) {
      throw new AlertException("generate.keypair.error");
    }
    if (keyTypeCode == KeyType.NODE.getCode()) {
      return KeypairCertBo.builder()
          .algorithm(keyType.getAlgo().getCode())
          .publicKey(publicKey)
          .privateKey(privateKey)
          .build();
    }

    String certStr = null, csrStr = null;
    try {
      if (keyTypeCode == KeyType.ROOT_CA.getCode()) {
        Certificate rootCA = CertUtil.generateRootCA(new X500Name(keyType.getSubject().getCode()),
            keyPair, CertUtil.getNotBefore(), CertUtil.getNotAfter(TTL_ROOT_CA_IN_DAY));
        certStr = CertUtil.certToPem(rootCA);
      } else {
        CertSignType signType = keyType.getSignType();
        Assert.isTrue(signType == CertSignType.BC || signType == CertSignType.VAULT,
            "sign.csr.not.supported");
        PKCS10CertificationRequest csr = CertUtil
            .generateCSR(new X500Name(keyType.getSubject().getCode()),
                keyPair.getPublic(), keyPair.getPrivate(), sanList);
        int ttl = keyTypeCode == KeyType.CONSORTIUM.getCode() ? TTL_CONSORTIUM_CA_IN_DAY
            : TTL_RPC_CERT_IN_DAY;
        if (keyType.getSignType() == CertSignType.BC) {
          Assert.notNull(bcCa, "parentKeypair.not.found");
          certStr = signCertRequestByBC(bcCa, csr, lastCrtExpire, ttl);
        }
        if (keyType.getSignType() == CertSignType.VAULT) {
          String issuer = keyTypeCode == KeyType.CONSORTIUM.getCode()
              ? X509CertConstant.VAULT_ROLE_ISSUER_CONS
              : X509CertConstant.VAULT_ROLE_ISSUER_RPC;
          certStr = signCertRequestByVault(vaultCa, csr, lastCrtExpire, ttl, issuer);
        }
        csrStr = CertUtil.csrToPem(csr);
      }
    } catch (Exception e) {
      throw new RuntimeException("generate.cert.error", e);
    }

    return KeypairCertBo.builder()
        .algorithm(keyType.getAlgo().getCode())
        .publicKey(publicKey)
        .privateKey(privateKey)
        .csrStr(csrStr)
        .certStr(certStr)
        .build();
  }

  private String signCertRequestByBC(KeypairPo parentKeypair, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl) throws Exception {
    PrivateKey parentPrivateKey = KeyUtil.privateKeyFromBase64(parentKeypair.getAlgorithm(),
        parentKeypair.getPrivateKey());
    byte[] parentCertBytes = CertUtil.certFromPem(parentKeypair.getCertStr()).getEncoded();
    int lastCertTtlInDay = getLastCrtTtlInDay(lastCrtExpire);
    Certificate cert = CertUtil.generateCert(csr, parentPrivateKey, parentCertBytes,
        CertUtil.getNotBefore(), CertUtil.getNotAfter(lastCertTtlInDay + ttl), true);
    return CertUtil.certToPem(cert);
  }

  private String signCertRequestByVault(String pkiPath, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl, String issuer) throws Exception {
    int lastCertTtlInDay = getLastCrtTtlInDay(lastCrtExpire);
    Map<String, String> displayNameMap = parseSubject(csr.getSubject());
    String cn = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));

    VaultCertificateRequest request = VaultCertificateRequest.builder()
        .ttl(Duration.ofDays(lastCertTtlInDay + ttl))
        .commonName(cn).build();
    VaultSignCertificateRequestResponse certificateResponse = vaultOperations
        .opsForPki(pkiPath)
        .signCertificateRequest(issuer, CertUtil.csrToPem(csr), request);
    org.springframework.vault.support.Certificate cert = certificateResponse.getRequiredData();
    return generatePem(cert.getCertificate());
  }

  private String generatePem(String base64Str) {
    Iterable<String> lines = Splitter.fixedLength(64).split(base64Str);
    StringBuilder content = new StringBuilder();
    for (String line : lines) {
      content.append(line).append("\n");
    }
    return "-----BEGIN CERTIFICATE-----\n" + content.toString() +
        "-----END CERTIFICATE-----";
  }

  private Map<String, String> parseSubject(X500Name subject) {
    String displayName = BCStyle.INSTANCE.toString(subject);
    Map<String, String> displayNameMap = Maps.newHashMap();
    Arrays.stream(StringUtils.split(displayName, ",")).forEach(item -> {
      String[] paras = item.split("=");
      displayNameMap.put(paras[0], paras[1]);
    });
    return displayNameMap;
  }

  private Map<String, String> parseSubject(String subjectDisplayName) {
    Map<String, String> displayNameMap = Maps.newHashMap();
    Arrays.stream(StringUtils.split(subjectDisplayName, ",")).forEach(item -> {
      String[] paras = item.split("=");
      displayNameMap.put(paras[0], paras[1]);
    });
    return displayNameMap;
  }

  private int getLastCrtTtlInDay(Date lastCrtExpire) {
    int lastCrtLeftDays = 0;
    if (lastCrtExpire != null) {
      lastCrtLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCrtExpire);
      lastCrtLeftDays = Math.max(lastCrtLeftDays, 0);
    }
    return lastCrtLeftDays;
  }

  public VaultDataGeneRootBo geneRootCaFromVault() {
    mountToVault(VAULT_PKI_PATH_ROOT_CA, TTL_ROOT_CA_IN_DAY);

    Map<String, String> displayNameMap = parseSubject(KeyType.ROOT_CA.getSubject().getCode());
    String cn = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));
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
    VaultResponse writeResponse = vaultOperations
        .write(VAULT_PKI_PATH_ROOT_CA + "/root/generate/exported", body);
    Assert.notNull(writeResponse, "gene.root.ca.not.response");

    Map<String, Object> dataMap = writeResponse.getData();
    return VaultDataGeneRootBo.builder().certificate((String) dataMap.get("certificate"))
        .private_key((String) dataMap.get("private_key"))
        .serial_number((String) dataMap.get("serial_number"))
        .build();
  }

  public void deleteRootCaFromVault() {
    vaultOperations.opsForPki().revoke("");

    VaultSysOperations adminOperations = vaultOperations.opsForSys();
    Map<String, VaultMount> mounts = adminOperations.getMounts();
    log.debug("vaultMount pkiPath:{},resp:{}", VAULT_PKI_PATH_ROOT_CA, JSON.toJSONString(mounts));
  }


  private void mountToVault(String pkiPath, int ttlInHour) {
    VaultSysOperations adminOperations = vaultOperations.opsForSys();
    if (adminOperations.getMounts().containsKey(pkiPath)) {
      return;
    }

    VaultMount mount = VaultMount.builder().type("pki")
        .config(Collections.singletonMap("max_lease_ttl", ttlInHour + "h")).description("bass pki")
        .build();
    adminOperations.mount(pkiPath, mount);

    Map<String, VaultMount> mounts = adminOperations.getMounts();
    log.debug("vaultMount pkiPath:{},resp:{}", pkiPath, JSON.toJSONString(mounts));
    Assert.isTrue(mounts.containsKey(pkiPath + "/"), "vault mount pki path error");
  }
}
