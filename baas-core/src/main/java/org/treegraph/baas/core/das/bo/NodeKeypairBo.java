package org.treegraph.baas.core.das.bo;

import java.io.Serializable;

import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zt
 * @date June 20, 2020
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeKeypairBo implements Serializable {
	
	private KeypairPo keypair;
	
	private NodeKeyPo nodeKey;
	
	private NodePo node;
	
    private static final long serialVersionUID = 1L;
}
