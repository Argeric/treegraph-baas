package org.treegraph.baas.core.domain;

import java.util.ArrayList;
import java.util.List;

public class MembershipPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public MembershipPoExample() {
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
      addCriterion("membership.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("membership.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(String value) {
      addCriterion("membership.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(String value) {
      addCriterion("membership.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(String value) {
      addCriterion("membership.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(String value) {
      addCriterion("membership.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(String value) {
      addCriterion("membership.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(String value) {
      addCriterion("membership.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLike(String value) {
      addCriterion("membership.id like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotLike(String value) {
      addCriterion("membership.id not like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<String> values) {
      addCriterion("membership.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<String> values) {
      addCriterion("membership.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(String value1, String value2) {
      addCriterion("membership.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(String value1, String value2) {
      addCriterion("membership.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdIsNull() {
      addCriterion("membership.membership_change_id is null");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdIsNotNull() {
      addCriterion("membership.membership_change_id is not null");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdEqualTo(Long value) {
      addCriterion("membership.membership_change_id =", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdNotEqualTo(Long value) {
      addCriterion("membership.membership_change_id <>", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdGreaterThan(Long value) {
      addCriterion("membership.membership_change_id >", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdGreaterThanOrEqualTo(Long value) {
      addCriterion("membership.membership_change_id >=", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdLessThan(Long value) {
      addCriterion("membership.membership_change_id <", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdLessThanOrEqualTo(Long value) {
      addCriterion("membership.membership_change_id <=", value, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdIn(List<Long> values) {
      addCriterion("membership.membership_change_id in", values, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdNotIn(List<Long> values) {
      addCriterion("membership.membership_change_id not in", values, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdBetween(Long value1, Long value2) {
      addCriterion("membership.membership_change_id between", value1, value2, "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andMembershipChangeIdNotBetween(Long value1, Long value2) {
      addCriterion("membership.membership_change_id not between", value1, value2,
          "membershipChangeId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("membership.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("membership.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("membership.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("membership.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("membership.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("membership.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("membership.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("membership.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("membership.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("membership.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("membership.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("membership.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("membership.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("membership.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andNodeIdIsNull() {
      addCriterion("membership.node_id is null");
      return (Criteria) this;
    }

    public Criteria andNodeIdIsNotNull() {
      addCriterion("membership.node_id is not null");
      return (Criteria) this;
    }

    public Criteria andNodeIdEqualTo(String value) {
      addCriterion("membership.node_id =", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotEqualTo(String value) {
      addCriterion("membership.node_id <>", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdGreaterThan(String value) {
      addCriterion("membership.node_id >", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
      addCriterion("membership.node_id >=", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLessThan(String value) {
      addCriterion("membership.node_id <", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLessThanOrEqualTo(String value) {
      addCriterion("membership.node_id <=", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLike(String value) {
      addCriterion("membership.node_id like", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotLike(String value) {
      addCriterion("membership.node_id not like", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdIn(List<String> values) {
      addCriterion("membership.node_id in", values, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotIn(List<String> values) {
      addCriterion("membership.node_id not in", values, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdBetween(String value1, String value2) {
      addCriterion("membership.node_id between", value1, value2, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotBetween(String value1, String value2) {
      addCriterion("membership.node_id not between", value1, value2, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIpIsNull() {
      addCriterion("membership.node_ip is null");
      return (Criteria) this;
    }

    public Criteria andNodeIpIsNotNull() {
      addCriterion("membership.node_ip is not null");
      return (Criteria) this;
    }

    public Criteria andNodeIpEqualTo(String value) {
      addCriterion("membership.node_ip =", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotEqualTo(String value) {
      addCriterion("membership.node_ip <>", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpGreaterThan(String value) {
      addCriterion("membership.node_ip >", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpGreaterThanOrEqualTo(String value) {
      addCriterion("membership.node_ip >=", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLessThan(String value) {
      addCriterion("membership.node_ip <", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLessThanOrEqualTo(String value) {
      addCriterion("membership.node_ip <=", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpLike(String value) {
      addCriterion("membership.node_ip like", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotLike(String value) {
      addCriterion("membership.node_ip not like", value, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpIn(List<String> values) {
      addCriterion("membership.node_ip in", values, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotIn(List<String> values) {
      addCriterion("membership.node_ip not in", values, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpBetween(String value1, String value2) {
      addCriterion("membership.node_ip between", value1, value2, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodeIpNotBetween(String value1, String value2) {
      addCriterion("membership.node_ip not between", value1, value2, "nodeIp");
      return (Criteria) this;
    }

    public Criteria andNodePortIsNull() {
      addCriterion("membership.node_port is null");
      return (Criteria) this;
    }

    public Criteria andNodePortIsNotNull() {
      addCriterion("membership.node_port is not null");
      return (Criteria) this;
    }

    public Criteria andNodePortEqualTo(Integer value) {
      addCriterion("membership.node_port =", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotEqualTo(Integer value) {
      addCriterion("membership.node_port <>", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortGreaterThan(Integer value) {
      addCriterion("membership.node_port >", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortGreaterThanOrEqualTo(Integer value) {
      addCriterion("membership.node_port >=", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortLessThan(Integer value) {
      addCriterion("membership.node_port <", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortLessThanOrEqualTo(Integer value) {
      addCriterion("membership.node_port <=", value, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortIn(List<Integer> values) {
      addCriterion("membership.node_port in", values, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotIn(List<Integer> values) {
      addCriterion("membership.node_port not in", values, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortBetween(Integer value1, Integer value2) {
      addCriterion("membership.node_port between", value1, value2, "nodePort");
      return (Criteria) this;
    }

    public Criteria andNodePortNotBetween(Integer value1, Integer value2) {
      addCriterion("membership.node_port not between", value1, value2, "nodePort");
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