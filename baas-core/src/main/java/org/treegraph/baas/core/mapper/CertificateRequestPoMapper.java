package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.CertificateRequestPo;
import org.treegraph.baas.core.domain.CertificateRequestPoExample;

public interface CertificateRequestPoMapper {
    long countByExample(CertificateRequestPoExample example);

    int deleteByExample(CertificateRequestPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CertificateRequestPo record);

    int insertSelective(CertificateRequestPo record);

    List<CertificateRequestPo> selectByExample(CertificateRequestPoExample example);

    Optional<CertificateRequestPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CertificateRequestPo record, @Param("example") CertificateRequestPoExample example);

    int updateByExample(@Param("record") CertificateRequestPo record, @Param("example") CertificateRequestPoExample example);

    int updateByPrimaryKeySelective(CertificateRequestPo record);

    int updateByPrimaryKey(CertificateRequestPo record);
}