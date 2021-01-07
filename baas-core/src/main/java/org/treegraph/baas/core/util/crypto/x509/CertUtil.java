package org.treegraph.baas.core.util.crypto.x509;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.util.DateUtils;

public class CertUtil {

  public static Certificate generateRoot(X500Name subject, KeyPair keyPair, Date notBefore,
      Date notAfter)
      throws Exception {
    SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
        .getInstance(keyPair.getPublic().getEncoded());
    BcX509ExtensionUtils extUtils = new BcX509ExtensionUtils();
    ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
    extensionsGenerator
        .addExtension(Extension.basicConstraints, true, new BasicConstraints(true)); // ca cert
    extensionsGenerator.addExtension(Extension.keyUsage, true,
        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
    extensionsGenerator.addExtension(Extension.subjectKeyIdentifier, false,
        extUtils.createSubjectKeyIdentifier(subjectPublicKeyInfo));
    extensionsGenerator.addExtension(Extension.authorityKeyIdentifier, false,
        extUtils.createAuthorityKeyIdentifier(subjectPublicKeyInfo));
    V3TBSCertificateGenerator tbsGen = new V3TBSCertificateGenerator();
    tbsGen.setSerialNumber(
        new ASN1Integer(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE));
    tbsGen.setIssuer(subject); // 自签证书颁发者等于使用者
    tbsGen.setStartDate(new Time(notBefore, Locale.CHINA));
    tbsGen.setEndDate(new Time(notAfter, Locale.CHINA));
    tbsGen.setSubject(subject);
    tbsGen.setSubjectPublicKeyInfo(subjectPublicKeyInfo);
    tbsGen.setExtensions(extensionsGenerator.generate());
    tbsGen.setSignature(getSignAlgo(subjectPublicKeyInfo.getAlgorithm())); // 签名算法标识等于密钥算法标识
    TBSCertificate tbs = tbsGen.generateTBSCertificate();
    return assembleCert(tbs, subjectPublicKeyInfo, keyPair.getPrivate());
  }

  public static PKCS10CertificationRequest generateCSR(X500Name subject, PublicKey publicKey,
      PrivateKey privateKey,
      List<String> sanList) {
    try {
      PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject,
          publicKey);
      if (CollectionUtils.isNotEmpty(sanList)) {
        // sanList去重
        Set<String> sanSet = Sets.newHashSet(sanList);
        List<GeneralName> generalNameList = sanSet.stream()
            .map(item -> new GeneralName(GeneralName.iPAddress, item))
            .collect(Collectors.toList());
        GeneralNames generalNames = new GeneralNames(generalNameList.toArray(new GeneralName[]{}));
        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        extensionsGenerator.addExtension(Extension.basicConstraints, true,
            new BasicConstraints(true)); // entity cert
        extensionsGenerator.addExtension(Extension.subjectAlternativeName, false, generalNames);
        builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
            extensionsGenerator.generate());
      }

      ContentSigner signGen;
      SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
          .getInstance(publicKey.getEncoded());
      AlgorithmIdentifier signAlgo = getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
      if (signAlgo.getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3)) {
        signGen = new JcaContentSignerBuilder("SM3WITHSM2").build(privateKey);
      } else if (signAlgo.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
        signGen = new JcaContentSignerBuilder("SHA256WITHECDSA").build(privateKey);
      } else if (signAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
        signGen = new JcaContentSignerBuilder("SHA256WITHRSA").build(privateKey);
      } else {
        throw new IllegalArgumentException("密钥算法不支持");
      }
      return builder.build(signGen);
    } catch (Exception e) {
      throw new RuntimeException("密钥结构错误", e);
    }
  }

  public static String signCertificate(KeypairPo parentKeypair, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl) throws Exception {
    PrivateKey parentPrivateKey = KeyUtil.privateKeyFromBase64(parentKeypair.getAlgorithm(),
        parentKeypair.getPrivateKey());
    byte[] parentCertBytes = CertUtil.certFromPem(parentKeypair.getCertStr()).getEncoded();
    int lastCertTtlInDay = CertUtil.getLastCrtTtlInDay(lastCrtExpire);
    Certificate cert = signCertificate(csr, parentPrivateKey, parentCertBytes,
        CertUtil.getNotBefore(), CertUtil.getNotAfter(lastCertTtlInDay + ttl), true);
    return CertUtil.certToPem(cert);
  }

  public static Certificate signCertificate(PKCS10CertificationRequest csr,
      PrivateKey issuerPrivateKey,
      byte[] issuerCert, Date notBefore, Date notAfter, boolean isCACert) throws Exception {
    X509CertificateHolder issuer = new X509CertificateHolder(issuerCert);
		if (!verifyCSR(csr)) {
			throw new IllegalArgumentException("证书请求验证失败");
		}
    X500Name subject = csr.getSubject();
    BcX509ExtensionUtils extUtils = new BcX509ExtensionUtils();
    ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
    extensionsGenerator.addExtension(Extension.basicConstraints, true,
        new BasicConstraints(isCACert)); // entity cert
    extensionsGenerator.addExtension(Extension.keyUsage, true,
        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign));
    extensionsGenerator.addExtension(Extension.authorityKeyIdentifier, false,
        extUtils.createAuthorityKeyIdentifier(issuer)); // 授权密钥标识
    extensionsGenerator.addExtension(Extension.subjectKeyIdentifier, false,
        extUtils.createSubjectKeyIdentifier(csr.getSubjectPublicKeyInfo())); // 使用者密钥标识

    Extensions extensions = null;
    for (Attribute attr : csr.getAttributes()) {
      if (PKCSObjectIdentifiers.pkcs_9_at_extensionRequest.equals(attr.getAttrType())) {
        extensions = Extensions.getInstance(attr.getAttributeValues()[0]);
        break;
      }
    }
    if (extensions != null) {
      extensionsGenerator.addExtension(extensions.getExtension(Extension.subjectAlternativeName));
    }

    V3TBSCertificateGenerator tbsGen = new V3TBSCertificateGenerator();
    tbsGen.setSerialNumber(
        new ASN1Integer(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE));
    tbsGen.setIssuer(issuer.getSubject());
    tbsGen.setStartDate(new Time(notBefore, Locale.CHINA));
    tbsGen.setEndDate(new Time(notAfter, Locale.CHINA));
    tbsGen.setSubject(subject);
    tbsGen.setSubjectPublicKeyInfo(csr.getSubjectPublicKeyInfo());
    tbsGen.setExtensions(extensionsGenerator.generate());
    tbsGen.setSignature(
        getSignAlgo(issuer.getSubjectPublicKeyInfo().getAlgorithm())); // 签名算法标识等于颁发者证书的密钥算法标识
    TBSCertificate tbs = tbsGen.generateTBSCertificate();
    return assembleCert(tbs, issuer.getSubjectPublicKeyInfo(), issuerPrivateKey);
  }

  public static String certToPem(Certificate x509Certificate) throws Exception {
    X509CertificateHolder holder = new X509CertificateHolder(x509Certificate);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
    pemWriter.writeObject(holder);
    pemWriter.flush();
    pemWriter.close();

    return bos.toString();
  }

  public static X509Certificate certFromPem(String certString) throws Exception {
    CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
    InputStream is = new ByteArrayInputStream(certString.getBytes(StandardCharsets.UTF_8));
    return (X509Certificate) certificate_factory.generateCertificate(is);
  }

  public static X509Certificate certFromPemFile(String pathToCert) throws Exception {
    CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
    FileInputStream fileInputstream = new FileInputStream(pathToCert);
    return (X509Certificate) certificate_factory.generateCertificate(fileInputstream);
  }

  public static String csrToPem(PKCS10CertificationRequest obj) throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
    pemWriter.writeObject(obj);
    pemWriter.flush();
    pemWriter.close();
    return bos.toString();
  }

  public static String certBase64ToPem(String base64Str) {
    Iterable<String> lines = Splitter.fixedLength(64).split(base64Str);
    StringBuilder content = new StringBuilder();
    for (String line : lines) {
      content.append(line).append("\n");
    }
    return "-----BEGIN CERTIFICATE-----\n" + content.toString() +
        "-----END CERTIFICATE-----";
  }

  public static AlgorithmIdentifier getSignAlgo(AlgorithmIdentifier algo) { // 根据公钥算法标识返回对应签名算法标识
    if (algo.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)
        && algo.getParameters().equals(GMObjectIdentifiers.sm2p256v1)) {
      return new AlgorithmIdentifier(GMObjectIdentifiers.sm2sign_with_sm3, DERNull.INSTANCE);
    } else if (algo.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)
        && algo.getParameters().equals(SECObjectIdentifiers.secp256k1)) {
      return new AlgorithmIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA256);
    } else if (algo.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
			return new AlgorithmIdentifier(PKCSObjectIdentifiers.sha256WithRSAEncryption,
					DERNull.INSTANCE);
		} else {
			throw new IllegalArgumentException("密钥算法不支持");
		}
  }

  public static Map<String, String> parseSubject(X500Name subject) {
    return parseSubject(BCStyle.INSTANCE.toString(subject));
  }

  public static Map<String, String> parseSubject(String subjectDisplayName) {
    Map<String, String> displayNameMap = Maps.newHashMap();
    Arrays.stream(StringUtils.split(subjectDisplayName, ",")).forEach(item -> {
      String[] paras = item.split("=");
      displayNameMap.put(paras[0], paras[1]);
    });
    return displayNameMap;
  }

  public static Date getNotBefore() {
    return new Date();
  }

  public static Date getNotAfter(int days) {
    return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L * days);
  }

  public static int getLastCrtTtlInDay(Date lastCrtExpire) {
    int lastCrtLeftDays = 0;
    if (lastCrtExpire != null) {
      lastCrtLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCrtExpire);
      lastCrtLeftDays = Math.max(lastCrtLeftDays, 0);
    }
    return lastCrtLeftDays;
  }

  private static Certificate assembleCert(TBSCertificate tbsCertificate,
      SubjectPublicKeyInfo issuerSubjectPublicKeyInfo, PrivateKey issuerPrivateKey)
      throws Exception {
    byte[] signature;
		if (issuerPrivateKey.getAlgorithm().equalsIgnoreCase("ECDSA")) {
			if (issuerSubjectPublicKeyInfo.getAlgorithm().getParameters()
					.equals(GMObjectIdentifiers.sm2p256v1)) {
				signature = SignUtil.SM2Sign(tbsCertificate.getEncoded(), issuerPrivateKey);
			} else if (issuerSubjectPublicKeyInfo.getAlgorithm().getParameters()
					.equals(SECObjectIdentifiers.secp256k1)) {
				signature = SignUtil.ECDSASign(tbsCertificate.getEncoded(), issuerPrivateKey);
			} else {
				throw new IllegalArgumentException("不支持的曲线");
			}
		} else if (issuerPrivateKey.getAlgorithm().equalsIgnoreCase("RSA")) {
			signature = SignUtil.RSASign(tbsCertificate.getEncoded(), issuerPrivateKey);
		} else {
			throw new IllegalArgumentException("不支持的密钥算法[" + issuerPrivateKey.getAlgorithm() + "]");
		}
    ASN1EncodableVector v = new ASN1EncodableVector();
    v.add(tbsCertificate);
    v.add(getSignAlgo(issuerSubjectPublicKeyInfo.getAlgorithm()));
    v.add(new DERBitString(signature));
    return Certificate.getInstance(new DERSequence(v));
  }

  private static boolean verifyCSR(PKCS10CertificationRequest csr) throws Exception {
    byte[] signature = csr.getSignature();
    if (csr.getSignatureAlgorithm().getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3)) {
      return SignUtil.SM2VerifySign(
          csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
          signature,
          KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo()));
    } else if (csr.getSignatureAlgorithm().getAlgorithm()
        .equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
      return SignUtil.ECDSAVerifySign(
          csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
          signature,
          KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo()));
    } else if (csr.getSignatureAlgorithm().getAlgorithm()
        .equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
      return SignUtil.RSAVerifySign(
          csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
          signature,
          KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo()));
    } else {
      throw new IllegalArgumentException("不支持的签名算法");
    }
  }
}
