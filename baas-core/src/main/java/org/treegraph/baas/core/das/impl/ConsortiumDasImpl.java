package org.treegraph.baas.core.das.impl;

import java.util.List;

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
}
