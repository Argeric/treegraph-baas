package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipChangePoExample;
import org.treegraph.baas.core.domain.MembershipChangePoExample.Criteria;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.mapper.primary.MembershipChangePoMapper;
import org.treegraph.baas.core.mapper.primary.MembershipPoMapper;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MembershipChangeDasImpl implements MembershipChangeDas {

  private MembershipChangePoMapper membershipChangePoMapper;

  private MembershipPoMapper membershipPoMapper;

  public Optional<MembershipChangePo> findById(long id) {
    return membershipChangePoMapper.selectByPrimaryKey(id);
  }

  public boolean addMembershipChange(MembershipChangePo membershipChange,
      List<MembershipPo> membershipList) {
    boolean succFlag = Boolean.TRUE;
    // add membershipChange
    succFlag = succFlag && membershipChangePoMapper.insert(membershipChange) > 0;
    // add membershipList
    membershipList.stream().forEach(item -> item.setMembershipChangeId(membershipChange.getId()));
    succFlag = succFlag && membershipPoMapper.bulkInsert(membershipList) == membershipList.size();
    return succFlag;
  }

  public boolean modifyMembershipChangeStatus(List<Long> membershipChangeIds,
      MembershipEnum.Status status) {
    MembershipChangePo record = MembershipChangePo.builder().status(status.getCode())
        .updateTime(LocalDateTime.now()).build();
    MembershipChangePoExample example = new MembershipChangePoExample();
    Criteria criteria = example.createCriteria();
    criteria.andIdIn(membershipChangeIds);
    return membershipChangePoMapper.updateByExampleSelective(record, example) == membershipChangeIds
        .size();
  }

  public List<MembershipChangePo> getMembershipChangeByStatus(List<Integer> statusList) {
    MembershipChangePoExample example = new MembershipChangePoExample();
    Criteria criteria = example.createCriteria();
    criteria.andStatusIn(statusList);
    List<MembershipChangePo> list = membershipChangePoMapper.selectByExample(example);
    return CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list;
  }
}
