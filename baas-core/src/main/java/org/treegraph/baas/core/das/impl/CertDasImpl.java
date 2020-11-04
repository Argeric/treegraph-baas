package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.CertDas;
import org.treegraph.baas.core.domain.CertificateRequestPo;
import org.treegraph.baas.core.domain.CertificateRequestPoExample;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.mapper.primary.CertificateRequestPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CertDasImpl implements CertDas {
	
	private CertificateRequestPoMapper certificateRequestPoMapper;
	
	public List<CertificateRequestPo> findByBlockchainId(String blockchainId){
		CertificateRequestPoExample example = new CertificateRequestPoExample();
		example.createCriteria().andBlockchainIdEqualTo(blockchainId);
		return certificateRequestPoMapper.selectByExample(example);
	}

	public boolean add(CertificateRequestPo certificateRequest){
		return certificateRequestPoMapper.insert(certificateRequest) > 0;
	}
}
