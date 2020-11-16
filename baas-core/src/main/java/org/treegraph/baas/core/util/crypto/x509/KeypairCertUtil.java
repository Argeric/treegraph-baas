package org.treegraph.baas.core.util.crypto.x509;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_ROOT_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_RPC_CERT_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_PKI_PATH_ROOT_CA;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.CertSignType;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.VaultService;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {

  private final VaultService vaultService;

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
    try {
      keyPair = KeyUtil.generateKeyPair(keyType.getAlgo().getCode());
      publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
      privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
    } catch (Exception e) {
      throw new AlertException("generate.keypair.error");
    }
    if (keyType == KeyType.NODE) {
      return KeypairCertBo.builder()
          .algorithm(keyType.getAlgo().getCode())
          .publicKey(publicKey)
          .privateKey(privateKey)
          .build();
    }

    String certStr = null, csrStr = null;
    try {
      if (keyType == KeyType.ROOT_CA) {
        if (keyType.getSignType() == CertSignType.BC) {
          Certificate rootCA = CertUtil.generateRoot(new X500Name(keyType.getSubject().getCode()),
              keyPair, CertUtil.getNotBefore(), CertUtil.getNotAfter(TTL_ROOT_CA_IN_DAY));
          certStr = CertUtil.certToPem(rootCA);
        }
        // add---> vault
        if (keyType.getSignType() == CertSignType.VAULT) {
          VaultGenerateRootBo vaultGenerateRootBo = vaultService.generateRoot();
          publicKey = null;
          privateKey = vaultGenerateRootBo.getPrivateKey();
          certStr = vaultGenerateRootBo.getCertificate();
        }
        // add---> vault
      } else {
        CertSignType signType = keyType.getSignType();
        Assert.isTrue(signType == CertSignType.BC || signType == CertSignType.VAULT,
            "sign.csr.not.supported");
        PKCS10CertificationRequest csr = CertUtil
            .generateCSR(new X500Name(keyType.getSubject().getCode()),
                keyPair.getPublic(), keyPair.getPrivate(), sanList);
        int ttl = keyType == KeyType.CONSORTIUM ? TTL_CONSORTIUM_CA_IN_DAY
            : TTL_RPC_CERT_IN_DAY;
        if (keyType.getSignType() == CertSignType.BC) {
          Assert.notNull(bcCa, "parentKeypair.not.found");
          certStr = CertUtil.signCertificate(bcCa, csr, lastCrtExpire, ttl);
        }
        if (keyType.getSignType() == CertSignType.VAULT) {
          String issuer = keyType == KeyType.CONSORTIUM
              ? X509CertConstant.VAULT_ROLE_CONS_CA_ISSUER
              : X509CertConstant.VAULT_ROLE_RPC_CERT_ISSUER;
          certStr = vaultService.signCertificate(vaultCa, csr, lastCrtExpire, ttl, issuer);
          // add---> vault
          VaultSignIntermediateBo vaultSignIntermediateBo = vaultService.signIntermediate(VAULT_PKI_PATH_ROOT_CA, CertUtil.csrToPem(csr));
          certStr = vaultSignIntermediateBo.getCertificate();
          // add---> vault
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
}
