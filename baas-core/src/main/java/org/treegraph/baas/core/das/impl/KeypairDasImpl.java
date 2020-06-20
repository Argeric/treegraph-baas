package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.KeypairPoExample;
import org.treegraph.baas.core.domain.KeypairPoExample.Criteria;
import org.treegraph.baas.core.mapper.primary.KeypairPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KeypairDasImpl implements KeypairDas {

	private KeypairPoMapper keypairPoMapper;
	
	public Optional<KeypairPo> findById(Long id) {
		return keypairPoMapper.selectByPrimaryKey(id);
	}
	
	public List<KeypairPo> findAll(String consId, Integer keyType, String algo) {
		KeypairPoExample example = new KeypairPoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if (StringUtils.isNotBlank(algo)) {
			criteria.andAlgorithmEqualTo(algo);
		}
		if(keyType != null){
			criteria.andKeyTypeEqualTo(keyType);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return keypairPoMapper.selectByExample(example);
	}
	
	public boolean save(KeypairPo keypair) {
		return keypairPoMapper.insert(keypair) > 0;
	}

	public boolean addKeypairs(List<KeypairPo> list) {
		return keypairPoMapper.bulkInsert(list) == list.size();
	}

	public boolean modifyKeypair(KeypairPo keypair) {
		return keypairPoMapper.updateByPrimaryKeySelective(keypair) > 0;
	}
}
