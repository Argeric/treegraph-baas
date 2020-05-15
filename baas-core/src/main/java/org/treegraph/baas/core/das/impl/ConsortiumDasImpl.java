package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.ConsortiumPoExample;
import org.treegraph.baas.core.mapper.ConsortiumPoMapper;

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
	
	public List<ConsortiumPo> findByIds(List<String> ids) {
		ConsortiumPoExample example = new ConsortiumPoExample();
		example.createCriteria().andIdIn(ids);
		return consortiumPoMapper.selectByExample(example);
	}
	
	public int createConsortium(ConsortiumPo cons) {
		return consortiumPoMapper.insert(cons);
	}
	
	public List<ConsortiumPo> findByName(String name){
		ConsortiumPoExample example = new ConsortiumPoExample();
		example.createCriteria().andNameEqualTo(name);
		return consortiumPoMapper.selectByExample(example);
	}
	
	public List<ConsortiumPo> findAll(){
		ConsortiumPoExample example = new ConsortiumPoExample();
		return consortiumPoMapper.selectByExample(example);
	}
	
	public int modifyCons(ConsortiumPo cons){
		return consortiumPoMapper.updateByPrimaryKeySelective(cons);
	}
	
	public Optional<ConsortiumPo> findById(String id) {
		return consortiumPoMapper.selectByPrimaryKey(id);
	}
}
