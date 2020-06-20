package org.treegraph.baas.core.das.impl;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.NodeKeyDas;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.mapper.primary.NodeKeyPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeKeyDasImpl implements NodeKeyDas {

	private NodeKeyPoMapper nodeKeyPoMapper;
	
	public boolean save(NodeKeyPo nodeKey){
		return nodeKeyPoMapper.insert(nodeKey) > 0;
	}
}
