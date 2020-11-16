package org.treegraph.baas.core.service;

import java.util.Date;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.treegraph.baas.core.service.bo.VaultGenerateRootBo;
import org.treegraph.baas.core.service.bo.VaultSignIntermediateBo;

public interface VaultService {

  boolean mount(String pkiPath, int ttlInHour);

  void createRole(String pkiPath, String roleName, int ttlInHour);

  VaultGenerateRootBo generateRoot();

  VaultSignIntermediateBo signIntermediate(String pkiPath, String csr);

  String signCertificate(String pkiPath, PKCS10CertificationRequest csr,
      Date lastCrtExpire, int ttl, String issuer) throws Exception;
}
