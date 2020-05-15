package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.ConsortiumPo;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
public interface ConsortiumDas {

	List<ConsortiumPo> findByIds(List<String> ids);
	
	int createConsortium(ConsortiumPo cons);
	
	List<ConsortiumPo> findByName(String name);
	
	List<ConsortiumPo> findAll();
	
	int modifyCons(ConsortiumPo cons);
	
	Optional<ConsortiumPo> findById(String id);
}
