package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.ConsortiumPo;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
public interface ConsortiumDas {

	List<ConsortiumPo> findByIds(List<String> ids);
}
