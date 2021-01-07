package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserOrganizationPoExample;

public interface UserOrganizationPoMapper {

  long countByExample(UserOrganizationPoExample example);

  int deleteByExample(UserOrganizationPoExample example);

  int deleteByPrimaryKey(Long id);

  int insert(UserOrganizationPo record);

  int insertSelective(UserOrganizationPo record);

  List<UserOrganizationPo> selectByExample(UserOrganizationPoExample example);

  UserOrganizationPo selectByPrimaryKey(Long id);

  int updateByExampleSelective(@Param("record") UserOrganizationPo record,
      @Param("example") UserOrganizationPoExample example);

  int updateByExample(@Param("record") UserOrganizationPo record,
      @Param("example") UserOrganizationPoExample example);

  int updateByPrimaryKeySelective(UserOrganizationPo record);

  int updateByPrimaryKey(UserOrganizationPo record);

  int bulkInsert(List<UserOrganizationPo> list);
}