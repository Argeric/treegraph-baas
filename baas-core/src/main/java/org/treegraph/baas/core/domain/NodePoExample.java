package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NodePoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public NodePoExample() {
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
      addCriterion("node.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("node.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(String value) {
      addCriterion("node.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(String value) {
      addCriterion("node.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(String value) {
      addCriterion("node.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(String value) {
      addCriterion("node.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(String value) {
      addCriterion("node.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(String value) {
      addCriterion("node.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLike(String value) {
      addCriterion("node.id like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotLike(String value) {
      addCriterion("node.id not like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<String> values) {
      addCriterion("node.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<String> values) {
      addCriterion("node.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(String value1, String value2) {
      addCriterion("node.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(String value1, String value2) {
      addCriterion("node.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("node.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("node.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("node.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("node.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("node.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("node.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("node.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("node.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("node.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("node.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("node.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("node.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("node.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("node.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNull() {
      addCriterion("node.blockchain_id is null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNotNull() {
      addCriterion("node.blockchain_id is not null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdEqualTo(String value) {
      addCriterion("node.blockchain_id =", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotEqualTo(String value) {
      addCriterion("node.blockchain_id <>", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThan(String value) {
      addCriterion("node.blockchain_id >", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThanOrEqualTo(String value) {
      addCriterion("node.blockchain_id >=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThan(String value) {
      addCriterion("node.blockchain_id <", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThanOrEqualTo(String value) {
      addCriterion("node.blockchain_id <=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLike(String value) {
      addCriterion("node.blockchain_id like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotLike(String value) {
      addCriterion("node.blockchain_id not like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIn(List<String> values) {
      addCriterion("node.blockchain_id in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotIn(List<String> values) {
      addCriterion("node.blockchain_id not in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdBetween(String value1, String value2) {
      addCriterion("node.blockchain_id between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotBetween(String value1, String value2) {
      addCriterion("node.blockchain_id not between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andNodeIpIsNull() {
      addCriterion("node.node_ip is null");
      return (Criteria) this;
    }

    public Criteria andNodeIpIsNotNull() {
      addCriterion("node.node_ip is not null");
      return (Criteria) this;
    }

    public Criteria andNodeIpEqualTo(String value) {
      addCriterion("node.node_ip =", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotEqualTo(String value) {
      addCriterion("node.node_ip <>", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpGreaterThan(String value) {
      addCriterion("node.node_ip >", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpGreaterThanOrEqualTo(String value) {
      addCriterion("node.node_ip >=", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLessThan(String value) {
      addCriterion("node.node_ip <", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLessThanOrEqualTo(String value) {
      addCriterion("node.node_ip <=", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLike(String value) {
      addCriterion("node.node_ip like", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotLike(String value) {
      addCriterion("node.node_ip not like", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpIn(List<String> values) {
      addCriterion("node.node_ip in", values, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotIn(List<String> values) {
      addCriterion("node.node_ip not in", values, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpBetween(String value1, String value2) {
      addCriterion("node.node_ip between", value1, value2, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotBetween(String value1, String value2) {
      addCriterion("node.node_ip not between", value1, value2, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodePortIsNull() {
      addCriterion("node.node_port is null");
      return (Criteria) this;
    }

    public Criteria andNodePortIsNotNull() {
      addCriterion("node.node_port is not null");
      return (Criteria) this;
    }

    public Criteria andNodePortEqualTo(Integer value) {
      addCriterion("node.node_port =", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotEqualTo(Integer value) {
      addCriterion("node.node_port <>", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortGreaterThan(Integer value) {
      addCriterion("node.node_port >", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortGreaterThanOrEqualTo(Integer value) {
      addCriterion("node.node_port >=", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortLessThan(Integer value) {
      addCriterion("node.node_port <", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortLessThanOrEqualTo(Integer value) {
      addCriterion("node.node_port <=", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortIn(List<Integer> values) {
      addCriterion("node.node_port in", values, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotIn(List<Integer> values) {
      addCriterion("node.node_port not in", values, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortBetween(Integer value1, Integer value2) {
      addCriterion("node.node_port between", value1, value2, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotBetween(Integer value1, Integer value2) {
      addCriterion("node.node_port not between", value1, value2, "nodePort");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameIsNull() {
      addCriterion("node.organization_name is null");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameIsNotNull() {
      addCriterion("node.organization_name is not null");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameEqualTo(String value) {
      addCriterion("node.organization_name =", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameNotEqualTo(String value) {
      addCriterion("node.organization_name <>", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameGreaterThan(String value) {
      addCriterion("node.organization_name >", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameGreaterThanOrEqualTo(String value) {
      addCriterion("node.organization_name >=", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameLessThan(String value) {
      addCriterion("node.organization_name <", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameLessThanOrEqualTo(String value) {
      addCriterion("node.organization_name <=", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameLike(String value) {
      addCriterion("node.organization_name like", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameNotLike(String value) {
      addCriterion("node.organization_name not like", value, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameIn(List<String> values) {
      addCriterion("node.organization_name in", values, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameNotIn(List<String> values) {
      addCriterion("node.organization_name not in", values, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameBetween(String value1, String value2) {
      addCriterion("node.organization_name between", value1, value2, "organizationName");
      return (Criteria) this;
    }

    public Criteria andOrganizationNameNotBetween(String value1, String value2) {
      addCriterion("node.organization_name not between", value1, value2, "organizationName");
      return (Criteria) this;
    }

    public Criteria andStatusIsNull() {
      addCriterion("node.`status` is null");
      return (Criteria) this;
    }

    public Criteria andStatusIsNotNull() {
      addCriterion("node.`status` is not null");
      return (Criteria) this;
    }

    public Criteria andStatusEqualTo(Integer value) {
      addCriterion("node.`status` =", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotEqualTo(Integer value) {
      addCriterion("node.`status` <>", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThan(Integer value) {
      addCriterion("node.`status` >", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("node.`status` >=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThan(Integer value) {
      addCriterion("node.`status` <", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThanOrEqualTo(Integer value) {
      addCriterion("node.`status` <=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusIn(List<Integer> values) {
      addCriterion("node.`status` in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotIn(List<Integer> values) {
      addCriterion("node.`status` not in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusBetween(Integer value1, Integer value2) {
      addCriterion("node.`status` between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("node.`status` not between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusIsNull() {
      addCriterion("node.membership_final_status is null");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusIsNotNull() {
      addCriterion("node.membership_final_status is not null");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusEqualTo(Integer value) {
      addCriterion("node.membership_final_status =", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusNotEqualTo(Integer value) {
      addCriterion("node.membership_final_status <>", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusGreaterThan(Integer value) {
      addCriterion("node.membership_final_status >", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("node.membership_final_status >=", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusLessThan(Integer value) {
      addCriterion("node.membership_final_status <", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusLessThanOrEqualTo(Integer value) {
      addCriterion("node.membership_final_status <=", value, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusIn(List<Integer> values) {
      addCriterion("node.membership_final_status in", values, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusNotIn(List<Integer> values) {
      addCriterion("node.membership_final_status not in", values, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusBetween(Integer value1, Integer value2) {
      addCriterion("node.membership_final_status between", value1, value2, "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipFinalStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("node.membership_final_status not between", value1, value2,
          "membershipFinalStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusIsNull() {
      addCriterion("node.membership_status is null");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusIsNotNull() {
      addCriterion("node.membership_status is not null");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusEqualTo(Integer value) {
      addCriterion("node.membership_status =", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusNotEqualTo(Integer value) {
      addCriterion("node.membership_status <>", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusGreaterThan(Integer value) {
      addCriterion("node.membership_status >", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("node.membership_status >=", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusLessThan(Integer value) {
      addCriterion("node.membership_status <", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusLessThanOrEqualTo(Integer value) {
      addCriterion("node.membership_status <=", value, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusIn(List<Integer> values) {
      addCriterion("node.membership_status in", values, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusNotIn(List<Integer> values) {
      addCriterion("node.membership_status not in", values, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusBetween(Integer value1, Integer value2) {
      addCriterion("node.membership_status between", value1, value2, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andMembershipStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("node.membership_status not between", value1, value2, "membershipStatus");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("node.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("node.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("node.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("node.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("node.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("node.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("node.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("node.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("node.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("node.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("node.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("node.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("node.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("node.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("node.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("node.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("node.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("node.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("node.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("node.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("node.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("node.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("node.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("node.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("node.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("node.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("node.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("node.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("node.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("node.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("node.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("node.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("node.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("node.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("node.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("node.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("node.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("node.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("node.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("node.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("node.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("node.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("node.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("node.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("node.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("node.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("node.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("node.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("node.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("node.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("node.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("node.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("node.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("node.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("node.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("node.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("node.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("node.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("node.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("node.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("node.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("node.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("node.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("node.update_by not between", value1, value2, "updateBy");
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