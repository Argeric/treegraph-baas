package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePo;

public interface ScanCustomizedFavoriteDas {

  Optional<ScanCustomizedFavoritePo> findById(Long id);

  List<ScanCustomizedFavoritePo> findByUserId(String userId);

  List<ScanCustomizedFavoritePo> findAll(String userId, String blockchainId);

  boolean add(ScanCustomizedFavoritePo scanCustomizedFavoritePo);

  boolean modify(ScanCustomizedFavoritePo scanCustomizedFavoritePo);

  boolean delete(Long favoriteId);
}
