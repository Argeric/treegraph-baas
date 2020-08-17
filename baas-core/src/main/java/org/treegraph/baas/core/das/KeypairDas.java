package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.das.bo.NodeKeypairInfoBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;

public interface KeypairDas {
	
	Optional<KeypairPo> findById(Long id);
	
	List<KeypairPo> findByIds(List<Long> ids);
	
	List<KeypairPo> findAll(String consId, Integer keyType, String algo);
	
	List<NodeKeypairInfoBo> findAll(String consId, Integer keyType, String algo, List<String> nodeIds);
	
	List<KeypairPo> findAll(List<String> consIds, Integer keyType, String algo);
	
	boolean save(KeypairPo keypair);
	
	boolean addKeypairs(List<KeypairPo> list);

	boolean modifyKeypair(KeypairPo keypair);
	
	boolean addKeyPairInfo(KeypairPo rootKeypair, KeypairPo consortiumKeypair, List<NodeKeypairBo> nodeKeypairBoList, List<NodeCustomizedConfigPo> customizedConfigList);
}
