package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NodeCustomizedConfigPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public NodeCustomizedConfigPoExample() {
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
      addCriterion("nodeCustomizedConfig.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("nodeCustomizedConfig.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("nodeCustomizedConfig.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("nodeCustomizedConfig.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("nodeCustomizedConfig.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("nodeCustomizedConfig.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("nodeCustomizedConfig.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("nodeCustomizedConfig.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("nodeCustomizedConfig.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("nodeCustomizedConfig.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("nodeCustomizedConfig.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("nodeCustomizedConfig.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("nodeCustomizedConfig.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("nodeCustomizedConfig.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("nodeCustomizedConfig.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.consortium_id not between", value1, value2,
          "consortiumId");
      return (Criteria) this;
    }

    public Criteria andNodeIdIsNull() {
      addCriterion("nodeCustomizedConfig.node_id is null");
      return (Criteria) this;
    }

    public Criteria andNodeIdIsNotNull() {
      addCriterion("nodeCustomizedConfig.node_id is not null");
      return (Criteria) this;
    }

    public Criteria andNodeIdEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.node_id =", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.node_id <>", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.node_id >", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.node_id >=", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLessThan(String value) {
      addCriterion("nodeCustomizedConfig.node_id <", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.node_id <=", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdLike(String value) {
      addCriterion("nodeCustomizedConfig.node_id like", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotLike(String value) {
      addCriterion("nodeCustomizedConfig.node_id not like", value, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.node_id in", values, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.node_id not in", values, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.node_id between", value1, value2, "nodeId");
      return (Criteria) this;
    }

    public Criteria andNodeIdNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.node_id not between", value1, value2, "nodeId");
      return (Criteria) this;
    }

    public Criteria andPropertyNameIsNull() {
      addCriterion("nodeCustomizedConfig.property_name is null");
      return (Criteria) this;
    }

    public Criteria andPropertyNameIsNotNull() {
      addCriterion("nodeCustomizedConfig.property_name is not null");
      return (Criteria) this;
    }

    public Criteria andPropertyNameEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_name =", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_name <>", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.property_name >", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_name >=", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameLessThan(String value) {
      addCriterion("nodeCustomizedConfig.property_name <", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_name <=", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameLike(String value) {
      addCriterion("nodeCustomizedConfig.property_name like", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameNotLike(String value) {
      addCriterion("nodeCustomizedConfig.property_name not like", value, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.property_name in", values, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.property_name not in", values, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.property_name between", value1, value2, "propertyName");
      return (Criteria) this;
    }

    public Criteria andPropertyNameNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.property_name not between", value1, value2,
          "propertyName");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("nodeCustomizedConfig.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("nodeCustomizedConfig.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("nodeCustomizedConfig.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("nodeCustomizedConfig.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("nodeCustomizedConfig.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("nodeCustomizedConfig.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("nodeCustomizedConfig.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("nodeCustomizedConfig.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("nodeCustomizedConfig.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("nodeCustomizedConfig.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("nodeCustomizedConfig.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("nodeCustomizedConfig.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("nodeCustomizedConfig.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("nodeCustomizedConfig.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("nodeCustomizedConfig.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("nodeCustomizedConfig.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("nodeCustomizedConfig.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("nodeCustomizedConfig.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("nodeCustomizedConfig.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("nodeCustomizedConfig.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("nodeCustomizedConfig.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("nodeCustomizedConfig.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("nodeCustomizedConfig.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("nodeCustomizedConfig.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("nodeCustomizedConfig.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("nodeCustomizedConfig.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("nodeCustomizedConfig.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("nodeCustomizedConfig.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("nodeCustomizedConfig.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("nodeCustomizedConfig.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.update_by not between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andPropertyValueIsNull() {
      addCriterion("nodeCustomizedConfig.property_value is null");
      return (Criteria) this;
    }

    public Criteria andPropertyValueIsNotNull() {
      addCriterion("nodeCustomizedConfig.property_value is not null");
      return (Criteria) this;
    }

    public Criteria andPropertyValueEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_value =", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueNotEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_value <>", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueGreaterThan(String value) {
      addCriterion("nodeCustomizedConfig.property_value >", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueGreaterThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_value >=", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueLessThan(String value) {
      addCriterion("nodeCustomizedConfig.property_value <", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueLessThanOrEqualTo(String value) {
      addCriterion("nodeCustomizedConfig.property_value <=", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueLike(String value) {
      addCriterion("nodeCustomizedConfig.property_value like", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueNotLike(String value) {
      addCriterion("nodeCustomizedConfig.property_value not like", value, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.property_value in", values, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueNotIn(List<String> values) {
      addCriterion("nodeCustomizedConfig.property_value not in", values, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.property_value between", value1, value2, "propertyValue");
      return (Criteria) this;
    }

    public Criteria andPropertyValueNotBetween(String value1, String value2) {
      addCriterion("nodeCustomizedConfig.property_value not between", value1, value2,
          "propertyValue");
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