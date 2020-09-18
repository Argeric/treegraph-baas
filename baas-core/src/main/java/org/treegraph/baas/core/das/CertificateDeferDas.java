package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.CertificateDeferPo;
import org.treegraph.baas.core.domain.KeypairPo;

public interface CertificateDeferDas {
	List<CertificateDeferPo> findByConsortiumId(String consId, List<Integer> statusList);
	
	int createCertificateDefer(CertificateDeferPo cons);
	
	int modifyCertificateDefer(CertificateDeferPo defer);
	
	boolean modifyCertificateDefer(CertificateDeferPo defer, List<Long> keypairIds, Integer deleteFlag,
			KeypairPo newKeypair);
}
