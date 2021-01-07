package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CertificateRequestPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public CertificateRequestPoExample() {
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
      addCriterion("certificateRequest.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("certificateRequest.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("certificateRequest.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("certificateRequest.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("certificateRequest.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("certificateRequest.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("certificateRequest.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("certificateRequest.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("certificateRequest.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("certificateRequest.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("certificateRequest.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("certificateRequest.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("certificateRequest.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("certificateRequest.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("certificateRequest.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("certificateRequest.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("certificateRequest.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("certificateRequest.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("certificateRequest.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("certificateRequest.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("certificateRequest.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("certificateRequest.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("certificateRequest.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNull() {
      addCriterion("certificateRequest.blockchain_id is null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIsNotNull() {
      addCriterion("certificateRequest.blockchain_id is not null");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdEqualTo(String value) {
      addCriterion("certificateRequest.blockchain_id =", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotEqualTo(String value) {
      addCriterion("certificateRequest.blockchain_id <>", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThan(String value) {
      addCriterion("certificateRequest.blockchain_id >", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.blockchain_id >=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThan(String value) {
      addCriterion("certificateRequest.blockchain_id <", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.blockchain_id <=", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdLike(String value) {
      addCriterion("certificateRequest.blockchain_id like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotLike(String value) {
      addCriterion("certificateRequest.blockchain_id not like", value, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdIn(List<String> values) {
      addCriterion("certificateRequest.blockchain_id in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotIn(List<String> values) {
      addCriterion("certificateRequest.blockchain_id not in", values, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdBetween(String value1, String value2) {
      addCriterion("certificateRequest.blockchain_id between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andBlockchainIdNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.blockchain_id not between", value1, value2, "blockchainId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdIsNull() {
      addCriterion("certificateRequest.apply_user_id is null");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdIsNotNull() {
      addCriterion("certificateRequest.apply_user_id is not null");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdEqualTo(String value) {
      addCriterion("certificateRequest.apply_user_id =", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdNotEqualTo(String value) {
      addCriterion("certificateRequest.apply_user_id <>", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdGreaterThan(String value) {
      addCriterion("certificateRequest.apply_user_id >", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.apply_user_id >=", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdLessThan(String value) {
      addCriterion("certificateRequest.apply_user_id <", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.apply_user_id <=", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdLike(String value) {
      addCriterion("certificateRequest.apply_user_id like", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdNotLike(String value) {
      addCriterion("certificateRequest.apply_user_id not like", value, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdIn(List<String> values) {
      addCriterion("certificateRequest.apply_user_id in", values, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdNotIn(List<String> values) {
      addCriterion("certificateRequest.apply_user_id not in", values, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdBetween(String value1, String value2) {
      addCriterion("certificateRequest.apply_user_id between", value1, value2, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andApplyUserIdNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.apply_user_id not between", value1, value2, "applyUserId");
      return (Criteria) this;
    }

    public Criteria andSanIsNull() {
      addCriterion("certificateRequest.san is null");
      return (Criteria) this;
    }

    public Criteria andSanIsNotNull() {
      addCriterion("certificateRequest.san is not null");
      return (Criteria) this;
    }

    public Criteria andSanEqualTo(String value) {
      addCriterion("certificateRequest.san =", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanNotEqualTo(String value) {
      addCriterion("certificateRequest.san <>", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanGreaterThan(String value) {
      addCriterion("certificateRequest.san >", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.san >=", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanLessThan(String value) {
      addCriterion("certificateRequest.san <", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.san <=", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanLike(String value) {
      addCriterion("certificateRequest.san like", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanNotLike(String value) {
      addCriterion("certificateRequest.san not like", value, "san");
      return (Criteria) this;
    }

    public Criteria andSanIn(List<String> values) {
      addCriterion("certificateRequest.san in", values, "san");
      return (Criteria) this;
    }

    public Criteria andSanNotIn(List<String> values) {
      addCriterion("certificateRequest.san not in", values, "san");
      return (Criteria) this;
    }

    public Criteria andSanBetween(String value1, String value2) {
      addCriterion("certificateRequest.san between", value1, value2, "san");
      return (Criteria) this;
    }

    public Criteria andSanNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.san not between", value1, value2, "san");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("certificateRequest.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("certificateRequest.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("certificateRequest.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("certificateRequest.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("certificateRequest.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("certificateRequest.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("certificateRequest.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("certificateRequest.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("certificateRequest.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("certificateRequest.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("certificateRequest.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("certificateRequest.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("certificateRequest.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("certificateRequest.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("certificateRequest.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("certificateRequest.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("certificateRequest.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("certificateRequest.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("certificateRequest.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("certificateRequest.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("certificateRequest.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("certificateRequest.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("certificateRequest.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("certificateRequest.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("certificateRequest.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("certificateRequest.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("certificateRequest.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("certificateRequest.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("certificateRequest.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("certificateRequest.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("certificateRequest.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("certificateRequest.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("certificateRequest.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("certificateRequest.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("certificateRequest.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("certificateRequest.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("certificateRequest.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("certificateRequest.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("certificateRequest.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("certificateRequest.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("certificateRequest.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("certificateRequest.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("certificateRequest.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("certificateRequest.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("certificateRequest.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("certificateRequest.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("certificateRequest.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("certificateRequest.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("certificateRequest.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("certificateRequest.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("certificateRequest.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("certificateRequest.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("certificateRequest.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("certificateRequest.update_by not between", value1, value2, "updateBy");
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