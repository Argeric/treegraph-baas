package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum;

public interface NodeDas {
	Optional<NodePo> findById(String id);
	
	List<NodePo> findByIds(List<String> id);
	/**/
	List<NodePo> findAll(String consId, String ip);
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId);
	
	List<NodePo> findByChainIds(List<String> chainIds);
	
	//NodePo findByIpPort(String ip, Integer port);
	
	long count();
	
	void addNodesMembershipChange(List<String> nodeIds, NodeEnum.MembershipStatus membershipStatus,
								  MembershipChangePo membershipChange, List<MembershipPo> memberships);
	
	boolean addNodes(List<NodePo> nodes, ConsortiumPo cons, List<NodeCustomizedConfigPo> stepFactorConfigList,
			List<NodeCustomizedConfigPo> configList);
	
	//boolean addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList, NodeCustomizedConfigPo stepFactorConfig,
	//		List<NodeCustomizedConfigPo> customizedConfigList);
	
	void addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList, List<NodeCustomizedConfigPo> stepFactorConfigList,
						 List<NodeCustomizedConfigPo> customizedConfigList, MembershipChangePo membershipChange,
						 List<MembershipPo> memberships);

	boolean modifyNode(NodePo node);
	
	boolean modifyNodesStatus(List<String> nodeIds, NodeEnum.Status status);
	
	void modifyNodesMembershipStatus(List<String> nodeIds, NodeEnum.MembershipStatus membershipStatus,
									 Long membershipChangeId, MembershipEnum.Status membershipChangeStatus);
	
	boolean deleteNodes(NodePo node, ConsortiumPo cons);
	
	//Integer deleteByIpPort(String ip, Integer port);
}
