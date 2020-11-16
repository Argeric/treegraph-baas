package org.treegraph.baas.core.service;

import java.util.Date;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.treegraph.baas.core.service.bo.VaultGenerateIntermediateBo;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;

public interface VaultService {

  boolean mount(String pkiPath, int ttlInHour);

  void createRole(String pkiPath, String roleName, int ttlInHour);

  VaultGenerateRootBo generateRoot();

  void submitCAInformation(String pkiPath, String certPem, String keyPem, int ttlInDay);

  VaultGenerateIntermediateBo generateIntermediate(String consPkiPath);

  VaultSignIntermediateBo signIntermediate(String csr);

  void setSignedIntermediate(String consPkiPath, String certPem);

  String signCertificate(String pkiPath, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl, String issuer) throws Exception;
}
