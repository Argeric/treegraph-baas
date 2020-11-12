package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.CertificateRequestPo;

public interface CertDas {

  List<CertificateRequestPo> findByBlockchainId(String blockchainId);

  boolean add(CertificateRequestPo certificateRequest);
}
