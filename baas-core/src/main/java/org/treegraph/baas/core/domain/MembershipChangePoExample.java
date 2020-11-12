package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MembershipChangePoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public MembershipChangePoExample() {
    oredCriteria = new ArrayList<>();
  }

  public void setOrderByClause(String orderByClause) {
    this.orderByClause = orderByClause;
  }

  public String getOrderByClause() {
    return orderByClause;
  }

  public void setDistinct(boolean distinct) {
    this.distinct = distinct;
  }

  public boolean isDistinct() {
    return distinct;
  }

  public List<Criteria> getOredCriteria() {
    return oredCriteria;
  }

  public void or(Criteria criteria) {
    oredCriteria.add(criteria);
  }

  public Criteria or() {
    Criteria criteria = createCriteriaInternal();
    oredCriteria.add(criteria);
    return criteria;
  }

  public Criteria createCriteria() {
    Criteria criteria = createCriteriaInternal();
    if (oredCriteria.size() == 0) {
      oredCriteria.add(criteria);
    }
    return criteria;
  }

  protected Criteria createCriteriaInternal() {
    Criteria criteria = new Criteria();
    return criteria;
  }

  public void clear() {
    oredCriteria.clear();
    orderByClause = null;
    distinct = false;
  }

  protected abstract static class GeneratedCriteria {

    protected List<Criterion> criteria;

    protected GeneratedCriteria() {
      super();
      criteria = new ArrayList<>();
    }

    public boolean isValid() {
      return criteria.size() > 0;
    }

    public List<Criterion> getAllCriteria() {
      return criteria;
    }

    public List<Criterion> getCriteria() {
      return criteria;
    }

    protected void addCriterion(String condition) {
      if (condition == null) {
        throw new RuntimeException("Value for condition cannot be null");
      }
      criteria.add(new Criterion(condition));
    }

    protected void addCriterion(String condition, Object value, String property) {
      if (value == null) {
        throw new RuntimeException("Value for " + property + " cannot be null");
      }
      criteria.add(new Criterion(condition, value));
    }

    protected void addCriterion(String condition, Object value1, Object value2, String property) {
      if (value1 == null || value2 == null) {
        throw new RuntimeException("Between values for " + property + " cannot be null");
      }
      criteria.add(new Criterion(condition, value1, value2));
    }

    public Criteria andIdIsNull() {
      addCriterion("membershipChange.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("membershipChange.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("membershipChange.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("membershipChange.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("membershipChange.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("membershipChange.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("membershipChange.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("membershipChange.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("membershipChange.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("membershipChange.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("membershipChange.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("membershipChange.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("membershipChange.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("membershipChange.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("membershipChange.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("membershipChange.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("membershipChange.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("membershipChange.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("membershipChange.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("membershipChange.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("membershipChange.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("membershipChange.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("membershipChange.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("membershipChange.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("membershipChange.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("membershipChange.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNull() {
      addCriterion("membershipChange.blockchain_id is null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNotNull() {
      addCriterion("membershipChange.blockchain_id is not null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdEqualTo(String value) {
      addCriterion("membershipChange.blockchain_id =", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotEqualTo(String value) {
      addCriterion("membershipChange.blockchain_id <>", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThan(String value) {
      addCriterion("membershipChange.blockchain_id >", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThanOrEqualTo(String value) {
      addCriterion("membershipChange.blockchain_id >=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThan(String value) {
      addCriterion("membershipChange.blockchain_id <", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThanOrEqualTo(String value) {
      addCriterion("membershipChange.blockchain_id <=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLike(String value) {
      addCriterion("membershipChange.blockchain_id like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotLike(String value) {
      addCriterion("membershipChange.blockchain_id not like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIn(List<String> values) {
      addCriterion("membershipChange.blockchain_id in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotIn(List<String> values) {
      addCriterion("membershipChange.blockchain_id not in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdBetween(String value1, String value2) {
      addCriterion("membershipChange.blockchain_id between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotBetween(String value1, String value2) {
      addCriterion("membershipChange.blockchain_id not between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andChangeTypeIsNull() {
      addCriterion("membershipChange.change_type is null");
      return (Criteria) this;
    }

    public Criteria andChangeTypeIsNotNull() {
      addCriterion("membershipChange.change_type is not null");
      return (Criteria) this;
    }

    public Criteria andChangeTypeEqualTo(Integer value) {
      addCriterion("membershipChange.change_type =", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeNotEqualTo(Integer value) {
      addCriterion("membershipChange.change_type <>", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeGreaterThan(Integer value) {
      addCriterion("membershipChange.change_type >", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeGreaterThanOrEqualTo(Integer value) {
      addCriterion("membershipChange.change_type >=", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeLessThan(Integer value) {
      addCriterion("membershipChange.change_type <", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeLessThanOrEqualTo(Integer value) {
      addCriterion("membershipChange.change_type <=", value, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeIn(List<Integer> values) {
      addCriterion("membershipChange.change_type in", values, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeNotIn(List<Integer> values) {
      addCriterion("membershipChange.change_type not in", values, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeBetween(Integer value1, Integer value2) {
      addCriterion("membershipChange.change_type between", value1, value2, "changeType");
      return (Criteria) this;
    }

    public Criteria andChangeTypeNotBetween(Integer value1, Integer value2) {
      addCriterion("membershipChange.change_type not between", value1, value2, "changeType");
      return (Criteria) this;
    }

    public Criteria andStatusIsNull() {
      addCriterion("membershipChange.`status` is null");
      return (Criteria) this;
    }

    public Criteria andStatusIsNotNull() {
      addCriterion("membershipChange.`status` is not null");
      return (Criteria) this;
    }

    public Criteria andStatusEqualTo(Integer value) {
      addCriterion("membershipChange.`status` =", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotEqualTo(Integer value) {
      addCriterion("membershipChange.`status` <>", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThan(Integer value) {
      addCriterion("membershipChange.`status` >", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("membershipChange.`status` >=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThan(Integer value) {
      addCriterion("membershipChange.`status` <", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThanOrEqualTo(Integer value) {
      addCriterion("membershipChange.`status` <=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusIn(List<Integer> values) {
      addCriterion("membershipChange.`status` in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotIn(List<Integer> values) {
      addCriterion("membershipChange.`status` not in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusBetween(Integer value1, Integer value2) {
      addCriterion("membershipChange.`status` between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("membershipChange.`status` not between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesIsNull() {
      addCriterion("membershipChange.committee_nodes is null");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesIsNotNull() {
      addCriterion("membershipChange.committee_nodes is not null");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesEqualTo(String value) {
      addCriterion("membershipChange.committee_nodes =", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesNotEqualTo(String value) {
      addCriterion("membershipChange.committee_nodes <>", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesGreaterThan(String value) {
      addCriterion("membershipChange.committee_nodes >", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesGreaterThanOrEqualTo(String value) {
      addCriterion("membershipChange.committee_nodes >=", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesLessThan(String value) {
      addCriterion("membershipChange.committee_nodes <", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesLessThanOrEqualTo(String value) {
      addCriterion("membershipChange.committee_nodes <=", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesLike(String value) {
      addCriterion("membershipChange.committee_nodes like", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesNotLike(String value) {
      addCriterion("membershipChange.committee_nodes not like", value, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesIn(List<String> values) {
      addCriterion("membershipChange.committee_nodes in", values, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesNotIn(List<String> values) {
      addCriterion("membershipChange.committee_nodes not in", values, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesBetween(String value1, String value2) {
      addCriterion("membershipChange.committee_nodes between", value1, value2, "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andCommitteeNodesNotBetween(String value1, String value2) {
      addCriterion("membershipChange.committee_nodes not between", value1, value2,
          "committeeNodes");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberIsNull() {
      addCriterion("membershipChange.pre_bft_epoch_number is null");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberIsNotNull() {
      addCriterion("membershipChange.pre_bft_epoch_number is not null");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberEqualTo(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number =", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberNotEqualTo(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number <>", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberGreaterThan(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number >", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberGreaterThanOrEqualTo(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number >=", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberLessThan(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number <", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberLessThanOrEqualTo(Long value) {
      addCriterion("membershipChange.pre_bft_epoch_number <=", value, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberIn(List<Long> values) {
      addCriterion("membershipChange.pre_bft_epoch_number in", values, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberNotIn(List<Long> values) {
      addCriterion("membershipChange.pre_bft_epoch_number not in", values, "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberBetween(Long value1, Long value2) {
      addCriterion("membershipChange.pre_bft_epoch_number between", value1, value2,
          "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andPreBftEpochNumberNotBetween(Long value1, Long value2) {
      addCriterion("membershipChange.pre_bft_epoch_number not between", value1, value2,
          "preBftEpochNumber");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("membershipChange.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("membershipChange.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("membershipChange.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("membershipChange.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("membershipChange.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("membershipChange.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("membershipChange.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("membershipChange.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("membershipChange.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("membershipChange.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("membershipChange.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("membershipChange.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("membershipChange.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("membershipChange.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("membershipChange.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("membershipChange.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("membershipChange.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("membershipChange.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("membershipChange.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("membershipChange.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("membershipChange.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("membershipChange.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("membershipChange.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("membershipChange.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("membershipChange.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("membershipChange.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("membershipChange.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("membershipChange.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("membershipChange.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("membershipChange.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("membershipChange.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("membershipChange.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("membershipChange.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("membershipChange.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("membershipChange.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("membershipChange.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("membershipChange.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("membershipChange.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("membershipChange.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("membershipChange.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("membershipChange.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("membershipChange.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("membershipChange.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("membershipChange.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("membershipChange.update_by not between", value1, value2, "updateBy");
      return (Criteria) this;
    }
  }

  public static class Criteria extends GeneratedCriteria {

    protected Criteria() {
      super();
    }
  }

  public static class Criterion {

    private String condition;

    private Object value;

    private Object secondValue;

    private boolean noValue;

    private boolean singleValue;

    private boolean betweenValue;

    private boolean listValue;

    private String typeHandler;

    public String getCondition() {
      return condition;
    }

    public Object getValue() {
      return value;
    }

    public Object getSecondValue() {
      return secondValue;
    }

    public boolean isNoValue() {
      return noValue;
    }

    public boolean isSingleValue() {
      return singleValue;
    }

    public boolean isBetweenValue() {
      return betweenValue;
    }

    public boolean isListValue() {
      return listValue;
    }

    public String getTypeHandler() {
      return typeHandler;
    }

    protected Criterion(String condition) {
      super();
      this.condition = condition;
      this.typeHandler = null;
      this.noValue = true;
    }

    protected Criterion(String condition, Object value, String typeHandler) {
      super();
      this.condition = condition;
      this.value = value;
      this.typeHandler = typeHandler;
      if (value instanceof List<?>) {
        this.listValue = true;
      } else {
        this.singleValue = true;
      }
    }

    protected Criterion(String condition, Object value) {
      this(condition, value, null);
    }

    protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
      super();
      this.condition = condition;
      this.value = value;
      this.secondValue = secondValue;
      this.typeHandler = typeHandler;
      this.betweenValue = true;
    }

    protected Criterion(String condition, Object value, Object secondValue) {
      this(condition, value, secondValue, null);
    }
  }
}