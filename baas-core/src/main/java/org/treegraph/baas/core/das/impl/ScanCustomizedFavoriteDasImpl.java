package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.ScanCustomizedFavoriteDas;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePo;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePoExample;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePoExample.Criteria;
import org.treegraph.baas.core.mapper.primary.ScanCustomizedFavoritePoMapper;

@Service
@AllArgsConstructor
public class ScanCustomizedFavoriteDasImpl implements ScanCustomizedFavoriteDas {

  private final ScanCustomizedFavoritePoMapper scanCustomizedFavoritePoMapper;

  public Optional<ScanCustomizedFavoritePo> findById(Long id) {
    return scanCustomizedFavoritePoMapper.selectByPrimaryKey(id);
  }

  public List<ScanCustomizedFavoritePo> findAll(String userId, String blockchainId) {
    ScanCustomizedFavoritePoExample example = new ScanCustomizedFavoritePoExample();
    Criteria criteria = example.createCriteria();
    criteria.andCreateByEqualTo(userId);
    if(blockchainId != null){
      criteria.andBlockchainIdEqualTo(blockchainId);
    }
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    return scanCustomizedFavoritePoMapper.selectByExample(example);
  }

  public List<ScanCustomizedFavoritePo> findByUserId(String userId) {
    ScanCustomizedFavoritePoExample example = new ScanCustomizedFavoritePoExample();
    example.createCriteria().andCreateByEqualTo(userId);
    example.setOrderByClause("create_time desc");
    return scanCustomizedFavoritePoMapper.selectByExample(example);
  }

  public boolean add(ScanCustomizedFavoritePo scanCustomizedFavoritePo) {
    return scanCustomizedFavoritePoMapper.insert(scanCustomizedFavoritePo) > 0;
  }

  public boolean modify(ScanCustomizedFavoritePo scanCustomizedFavoritePo) {
    return scanCustomizedFavoritePoMapper.updateByPrimaryKeySelective(scanCustomizedFavoritePo) > 0;
  }

  public boolean delete(Long favoriteId) {
    ScanCustomizedFavoritePo record = ScanCustomizedFavoritePo.builder()
        .id(favoriteId)
        .deleteFlag(IntTypeEnum.TRUE.getValue())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();
    return scanCustomizedFavoritePoMapper.updateByPrimaryKeySelective(record) > 0;
  }
}
