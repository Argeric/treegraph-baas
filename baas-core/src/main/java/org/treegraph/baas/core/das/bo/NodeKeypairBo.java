package org.treegraph.baas.core.das.bo;

import java.io.Serializable;

import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.treegraph.baas.core.domain.NodePo;

/**
 * @author zt
 * @version 1.0
 * @date June 20, 2020
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
