package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.ConsortiumPoExample;
import org.treegraph.baas.core.domain.ConsortiumPoExample.Criteria;
import org.treegraph.baas.core.mapper.ConsortiumPoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ConsortiumDasImpl implements ConsortiumDas{

	private ConsortiumPoMapper consortiumPoMapper;
	
	public Optional<ConsortiumPo> findById(String id) {
		return consortiumPoMapper.selectByPrimaryKey(id);
	}
	
	public List<ConsortiumPo> findByIds(List<String> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			return Lists.newArrayList();
		}
		ConsortiumPoExample example = new ConsortiumPoExample();
		example.createCriteria().andIdIn(ids);
		return consortiumPoMapper.selectByExample(example);
	}
	
	public List<ConsortiumPo> findByName(String name){
		ConsortiumPoExample example = new ConsortiumPoExample();
		example.createCriteria().andNameEqualTo(name)
		.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return consortiumPoMapper.selectByExample(example);
	}
	
	public List<ConsortiumPo> findAll(String id){
		ConsortiumPoExample example = new ConsortiumPoExample();
		Criteria criteria = example.createCriteria();
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		if(id != null) {
			criteria.andIdEqualTo(id);
		}
		return consortiumPoMapper.selectByExampleWithBLOBs(example);
	}
	
	public int createConsortium(ConsortiumPo cons) {
		return consortiumPoMapper.insert(cons);
	}
	
	public int modifyCons(ConsortiumPo cons){
		return consortiumPoMapper.updateByPrimaryKeySelective(cons);
	}

}
