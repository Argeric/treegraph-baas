package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePo;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePoExample;

public interface ScanCustomizedFavoritePoMapper {
    long countByExample(ScanCustomizedFavoritePoExample example);

    int deleteByExample(ScanCustomizedFavoritePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ScanCustomizedFavoritePo record);

    int insertSelective(ScanCustomizedFavoritePo record);

    List<ScanCustomizedFavoritePo> selectByExample(ScanCustomizedFavoritePoExample example);

    Optional<ScanCustomizedFavoritePo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ScanCustomizedFavoritePo record, @Param("example") ScanCustomizedFavoritePoExample example);

    int updateByExample(@Param("record") ScanCustomizedFavoritePo record, @Param("example") ScanCustomizedFavoritePoExample example);

    int updateByPrimaryKeySelective(ScanCustomizedFavoritePo record);

    int updateByPrimaryKey(ScanCustomizedFavoritePo record);
}