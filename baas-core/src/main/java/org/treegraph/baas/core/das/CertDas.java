package org.treegraph.baas.core.das;

import java.util.List;

public interface CertDas {
	List<CertificateRequestPo> findByBlockchainId(String blockchainId);
}
