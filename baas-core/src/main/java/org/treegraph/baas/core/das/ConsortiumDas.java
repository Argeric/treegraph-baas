package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.ConsortiumPo;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
public interface ConsortiumDas {

  Optional<ConsortiumPo> findById(String id);

  List<ConsortiumPo> findByIds(List<String> ids);

  List<ConsortiumPo> findByName(String name);

  List<ConsortiumPo> findAll(String id);

  long count();

  void createConsortium(ConsortiumPo cons);

  int modifyCons(ConsortiumPo cons);

}
