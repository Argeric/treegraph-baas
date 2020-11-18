package org.treegraph.baas.core.util.crypto.x509;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_ROOT_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_RPC_CERT_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.VAULT_ROLE_RPC_CERT_ISSUER;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConsortiumConstant;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.enums.CryptoEnum.CertSignType;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.VaultService;
import org.treegraph.baas.core.service.bo.KeypairBo;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.service.bo.VaultGenerateIntermediateBo;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {

  private final KeypairDas keypairDas;

  private final VaultService vaultService;

  public KeypairCertBo buildRoot() throws Exception{
    KeyType keyType = KeyType.ROOT_CA;
    String certStr = null, publicKey = null, privateKey = null;
    if (keyType.getSignType() == CertSignType.BC) {
      KeypairBo keypairBo = getKeypairByKeyType(keyType);
      Certificate rootCA = CertUtil.generateRoot(new X500Name(keyType.getSubject().getCode()),
          keypairBo.getKeypair(), CertUtil.getNotBefore(),
          CertUtil.getNotAfter(TTL_ROOT_CA_IN_DAY));
      certStr = CertUtil.certToPem(rootCA);
      publicKey = keypairBo.getPublicKey();
      privateKey = keypairBo.getPrivateKey();
    }
    if (keyType.getSignType() == CertSignType.VAULT) {
      VaultGenerateRootBo vaultGenerateRootBo = vaultService.generateRoot();
      certStr = vaultGenerateRootBo.getCertificate();
      publicKey = null;
      privateKey = vaultGenerateRootBo.getPrivateKey();
    }
    return KeypairCertBo.builder()
        .keyType(keyType)
        .publicKey(publicKey)
        .privateKey(privateKey)
        .csrStr(null)
        .certStr(certStr)
        .build();
  }

  public KeypairCertBo buildConsCA(String consId, Date lastCrtExpire) throws Exception {
    KeyType keyType = KeyType.CONSORTIUM;
    String publicKey = null, privateKey = null, csrPem = null, certPem = null;
    if (keyType.getSignType() == CertSignType.BC) {
      KeypairPo bcRootCa = getCA(ConsortiumConstant.DEFAULT_CONSORTIUM_ID, KeyType.ROOT_CA);
      Assert.notNull(bcRootCa, "bcRootCa.is.null");
      KeypairBo keypairBo = getKeypairByKeyType(keyType);
      publicKey = keypairBo.getPublicKey();
      privateKey = keypairBo.getPrivateKey();
      PKCS10CertificationRequest csr = CertUtil
          .generateCSR(new X500Name(keyType.getSubject().getCode()),
              keypairBo.getKeypair().getPublic(),
              keypairBo.getKeypair().getPrivate(), null);
      csrPem = CertUtil.csrToPem(csr);
      certPem = CertUtil.signCertificate(bcRootCa, csr, lastCrtExpire, TTL_CONSORTIUM_CA_IN_DAY);
      String privatePem = KeyUtil.privateKeyToPem(keypairBo.getKeypair().getPrivate());
      vaultService.submitCAInformation(consId, certPem, privatePem, TTL_CONSORTIUM_CA_IN_DAY);
    }
    if (keyType.getSignType() == CertSignType.VAULT) {
      VaultGenerateIntermediateBo generateIntermediate = vaultService.generateIntermediate(consId);
      publicKey = null;
      privateKey = generateIntermediate.getPrivateKey();
      csrPem = generateIntermediate.getCsr();
      VaultSignIntermediateBo signIntermediate = vaultService.signIntermediate(csrPem);
      certPem = signIntermediate.getCertificate();
      vaultService.setSignedIntermediate(consId, certPem);
    }
    return KeypairCertBo.builder()
        .keyType(keyType)
        .publicKey(publicKey)
        .privateKey(privateKey)
        .csrStr(csrPem)
        .certStr(certPem)
        .build();
  }

  public KeypairCertBo buildNodeKeypair() {
    KeyType keyType = KeyType.NODE;
    KeypairBo keypairBo = getKeypairByKeyType(keyType);
    return KeypairCertBo.builder()
        .keyType(keyType)
        .publicKey(keypairBo.getPublicKey())
        .privateKey(keypairBo.getPrivateKey())
        .build();
  }

  public KeypairCertBo buildRpcCert(String consId, Date lastCrtExpire, List<String> sanList)
      throws Exception {
    KeyType keyType = KeyType.RPC;
    KeypairBo keypair = getKeypairByKeyType(keyType);
    PKCS10CertificationRequest csr = CertUtil
        .generateCSR(new X500Name(keyType.getSubject().getCode()),
            keypair.getKeypair().getPublic(), keypair.getKeypair().getPrivate(), sanList);
    String certStr = null;
    if (keyType.getSignType() == CertSignType.BC) {
      KeypairPo bcConsCa = getCA(consId, KeyType.CONSORTIUM);
      Assert.notNull(bcConsCa, "bcConsCa.is.null");
      certStr = CertUtil.signCertificate(bcConsCa, csr, lastCrtExpire, TTL_RPC_CERT_IN_DAY);
    }
    if (keyType.getSignType() == CertSignType.VAULT) {
      certStr = vaultService.signCertificate(consId, csr, lastCrtExpire, TTL_RPC_CERT_IN_DAY,
          VAULT_ROLE_RPC_CERT_ISSUER);
    }
    return KeypairCertBo.builder()
        .keyType(keyType)
        .publicKey(keypair.getPublicKey())
        .privateKey(keypair.getPrivateKey())
        .csrStr(CertUtil.csrToPem(csr))
        .certStr(certStr)
        .build();
  }

  private KeypairBo getKeypairByKeyType(KeyType keyType) {
    KeyPair keypair;
    String publicKey;
    String privateKey;
    try {
      keypair = KeyUtil.generateKeyPair(keyType.getAlgo().getCode());
      publicKey = KeyUtil.publicKeyToBase64(keypair.getPublic());
      privateKey = KeyUtil.privateKeyToBase64(keypair.getPrivate());
    } catch (Exception e) {
      throw new AlertException("generate.keypair.error");
    }
    return KeypairBo.builder().keypair(keypair).publicKey(publicKey).privateKey(privateKey).build();
  }

  private KeypairPo getCA(String consId, KeyType keyType) {
    List<KeypairPo> rootKeypairList = keypairDas
        .findAll(consId, keyType.getCode(), keyType.getAlgo().getCode());
    if (CollectionUtils.isNotEmpty(rootKeypairList)) {
      return rootKeypairList.get(0);
    }
    return null;
  }
}
