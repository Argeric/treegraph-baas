package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.CertificateDeferPo;
import org.treegraph.baas.core.domain.CertificateDeferPoExample;

public interface CertificateDeferPoMapper {

  long countByExample(CertificateDeferPoExample example);

  int deleteByExample(CertificateDeferPoExample example);

  int deleteByPrimaryKey(Long id);

  int insert(CertificateDeferPo record);

  int insertSelective(CertificateDeferPo record);

  List<CertificateDeferPo> selectByExample(CertificateDeferPoExample example);

  CertificateDeferPo selectByPrimaryKey(Long id);

  int updateByExampleSelective(@Param("record") CertificateDeferPo record,
      @Param("example") CertificateDeferPoExample example);

  int updateByExample(@Param("record") CertificateDeferPo record,
      @Param("example") CertificateDeferPoExample example);

  int updateByPrimaryKeySelective(CertificateDeferPo record);

  int updateByPrimaryKey(CertificateDeferPo record);
}