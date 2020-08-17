package org.treegraph.baas.core.das.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.das.bo.NodeKeypairInfoBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.KeypairPoExample;
import org.treegraph.baas.core.domain.KeypairPoExample.Criteria;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.mapper.primary.KeypairPoMapper;
import org.treegraph.baas.core.mapper.primary.NodeCustomizedConfigPoMapper;
import org.treegraph.baas.core.mapper.primary.NodeKeyPoMapper;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KeypairDasImpl implements KeypairDas {

	private KeypairPoMapper keypairPoMapper;
	
	private NodeKeyPoMapper nodeKeyPoMapper;
	
	private NodeCustomizedConfigPoMapper nodeCustomizedConfigPoMapper;
	
	public Optional<KeypairPo> findById(Long id) {
		return keypairPoMapper.selectByPrimaryKey(id);
	}
	
	public List<KeypairPo> findByIds(List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			return Lists.newArrayList();
		}
		
		KeypairPoExample example = new KeypairPoExample();
		example.createCriteria().andIdIn(ids);
		example.setOrderByClause("create_time desc");
		return keypairPoMapper.selectByExample(example);
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
	
	public List<NodeKeypairInfoBo> findAll(String consId, Integer keyType, String algo, List<String> nodeIds) {
		return keypairPoMapper.selectAllByNodeIds(consId, keyType, algo, nodeIds);
	}
	
	public List<KeypairPo> findAll(List<String> consIds, Integer keyType, String algo) {
		KeypairPoExample example = new KeypairPoExample();
		Criteria criteria = example.createCriteria();
		if (CollectionUtils.isNotEmpty(consIds)) {
			criteria.andConsortiumIdIn(consIds);
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
	
	public boolean addKeyPairInfo(KeypairPo rootKeypair, KeypairPo consortiumKeypair,
			List<NodeKeypairBo> nodeKeypairBoList, List<NodeCustomizedConfigPo> customizedConfigList) {
		// add key pair of consortium
		boolean succFlag =  Boolean.TRUE;
		List<KeypairPo> keypairList = new ArrayList<>();
		if(rootKeypair.getId() == null){
			keypairList.add(rootKeypair);
		}
		if(consortiumKeypair.getId() == null){
			keypairList.add(consortiumKeypair);
		}
		if(CollectionUtils.isNotEmpty(keypairList)){
			succFlag = succFlag && keypairPoMapper.bulkInsert(keypairList) == keypairList.size();
		}
		
		// add key pair of node
		if(CollectionUtils.isNotEmpty(nodeKeypairBoList)) {
			boolean addSuccFlag = addKeyPairInfo(nodeKeypairBoList);
			succFlag = succFlag && addSuccFlag;
		}
		
		// add customized configration
		if(CollectionUtils.isNotEmpty(customizedConfigList)) {
			succFlag = succFlag
					&& nodeCustomizedConfigPoMapper.bulkInsert(customizedConfigList) == customizedConfigList.size();
		}
		
		
		return succFlag;
	}
	
	private boolean addKeyPairInfo(List<NodeKeypairBo> nodeKeypairBoList) {
		// add key pair of consortium
		boolean succFlag =  Boolean.TRUE;
		// add key pair of node
		for(NodeKeypairBo nodeKeypairBo : nodeKeypairBoList){
			if(nodeKeypairBo.getKeypair().getId() == null){
				KeypairPo keypair = nodeKeypairBo.getKeypair();
				NodeKeyPo nodeKey = nodeKeypairBo.getNodeKey();
				succFlag = succFlag && keypairPoMapper.insert(keypair) > 0;
				nodeKey.setKeypairId(keypair.getId());
				succFlag = succFlag && nodeKeyPoMapper.insert(nodeKey) > 0;
			}
		}
		return succFlag;
	}
}
