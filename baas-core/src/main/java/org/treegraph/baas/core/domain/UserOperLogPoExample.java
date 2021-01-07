package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserOperLogPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public UserOperLogPoExample() {
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
      addCriterion("userOperLog.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("userOperLog.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("userOperLog.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("userOperLog.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("userOperLog.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("userOperLog.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("userOperLog.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("userOperLog.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("userOperLog.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("userOperLog.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("userOperLog.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("userOperLog.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andUserIdIsNull() {
      addCriterion("userOperLog.user_id is null");
      return (Criteria) this;
    }

    public Criteria andUserIdIsNotNull() {
      addCriterion("userOperLog.user_id is not null");
      return (Criteria) this;
    }

    public Criteria andUserIdEqualTo(String value) {
      addCriterion("userOperLog.user_id =", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdNotEqualTo(String value) {
      addCriterion("userOperLog.user_id <>", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdGreaterThan(String value) {
      addCriterion("userOperLog.user_id >", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_id >=", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdLessThan(String value) {
      addCriterion("userOperLog.user_id <", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_id <=", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdLike(String value) {
      addCriterion("userOperLog.user_id like", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdNotLike(String value) {
      addCriterion("userOperLog.user_id not like", value, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdIn(List<String> values) {
      addCriterion("userOperLog.user_id in", values, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdNotIn(List<String> values) {
      addCriterion("userOperLog.user_id not in", values, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdBetween(String value1, String value2) {
      addCriterion("userOperLog.user_id between", value1, value2, "userId");
      return (Criteria) this;
    }

    public Criteria andUserIdNotBetween(String value1, String value2) {
      addCriterion("userOperLog.user_id not between", value1, value2, "userId");
      return (Criteria) this;
    }

    public Criteria andUserNameIsNull() {
      addCriterion("userOperLog.user_name is null");
      return (Criteria) this;
    }

    public Criteria andUserNameIsNotNull() {
      addCriterion("userOperLog.user_name is not null");
      return (Criteria) this;
    }

    public Criteria andUserNameEqualTo(String value) {
      addCriterion("userOperLog.user_name =", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameNotEqualTo(String value) {
      addCriterion("userOperLog.user_name <>", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameGreaterThan(String value) {
      addCriterion("userOperLog.user_name >", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_name >=", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameLessThan(String value) {
      addCriterion("userOperLog.user_name <", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_name <=", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameLike(String value) {
      addCriterion("userOperLog.user_name like", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameNotLike(String value) {
      addCriterion("userOperLog.user_name not like", value, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameIn(List<String> values) {
      addCriterion("userOperLog.user_name in", values, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameNotIn(List<String> values) {
      addCriterion("userOperLog.user_name not in", values, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameBetween(String value1, String value2) {
      addCriterion("userOperLog.user_name between", value1, value2, "userName");
      return (Criteria) this;
    }

    public Criteria andUserNameNotBetween(String value1, String value2) {
      addCriterion("userOperLog.user_name not between", value1, value2, "userName");
      return (Criteria) this;
    }

    public Criteria andUserIpIsNull() {
      addCriterion("userOperLog.user_ip is null");
      return (Criteria) this;
    }

    public Criteria andUserIpIsNotNull() {
      addCriterion("userOperLog.user_ip is not null");
      return (Criteria) this;
    }

    public Criteria andUserIpEqualTo(String value) {
      addCriterion("userOperLog.user_ip =", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpNotEqualTo(String value) {
      addCriterion("userOperLog.user_ip <>", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpGreaterThan(String value) {
      addCriterion("userOperLog.user_ip >", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_ip >=", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpLessThan(String value) {
      addCriterion("userOperLog.user_ip <", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_ip <=", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpLike(String value) {
      addCriterion("userOperLog.user_ip like", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpNotLike(String value) {
      addCriterion("userOperLog.user_ip not like", value, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpIn(List<String> values) {
      addCriterion("userOperLog.user_ip in", values, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpNotIn(List<String> values) {
      addCriterion("userOperLog.user_ip not in", values, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpBetween(String value1, String value2) {
      addCriterion("userOperLog.user_ip between", value1, value2, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserIpNotBetween(String value1, String value2) {
      addCriterion("userOperLog.user_ip not between", value1, value2, "userIp");
      return (Criteria) this;
    }

    public Criteria andUserDeviceIsNull() {
      addCriterion("userOperLog.user_device is null");
      return (Criteria) this;
    }

    public Criteria andUserDeviceIsNotNull() {
      addCriterion("userOperLog.user_device is not null");
      return (Criteria) this;
    }

    public Criteria andUserDeviceEqualTo(String value) {
      addCriterion("userOperLog.user_device =", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceNotEqualTo(String value) {
      addCriterion("userOperLog.user_device <>", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceGreaterThan(String value) {
      addCriterion("userOperLog.user_device >", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_device >=", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceLessThan(String value) {
      addCriterion("userOperLog.user_device <", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.user_device <=", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceLike(String value) {
      addCriterion("userOperLog.user_device like", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceNotLike(String value) {
      addCriterion("userOperLog.user_device not like", value, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceIn(List<String> values) {
      addCriterion("userOperLog.user_device in", values, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceNotIn(List<String> values) {
      addCriterion("userOperLog.user_device not in", values, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceBetween(String value1, String value2) {
      addCriterion("userOperLog.user_device between", value1, value2, "userDevice");
      return (Criteria) this;
    }

    public Criteria andUserDeviceNotBetween(String value1, String value2) {
      addCriterion("userOperLog.user_device not between", value1, value2, "userDevice");
      return (Criteria) this;
    }

    public Criteria andOperTypeIsNull() {
      addCriterion("userOperLog.oper_type is null");
      return (Criteria) this;
    }

    public Criteria andOperTypeIsNotNull() {
      addCriterion("userOperLog.oper_type is not null");
      return (Criteria) this;
    }

    public Criteria andOperTypeEqualTo(Integer value) {
      addCriterion("userOperLog.oper_type =", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeNotEqualTo(Integer value) {
      addCriterion("userOperLog.oper_type <>", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeGreaterThan(Integer value) {
      addCriterion("userOperLog.oper_type >", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeGreaterThanOrEqualTo(Integer value) {
      addCriterion("userOperLog.oper_type >=", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeLessThan(Integer value) {
      addCriterion("userOperLog.oper_type <", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeLessThanOrEqualTo(Integer value) {
      addCriterion("userOperLog.oper_type <=", value, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeIn(List<Integer> values) {
      addCriterion("userOperLog.oper_type in", values, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeNotIn(List<Integer> values) {
      addCriterion("userOperLog.oper_type not in", values, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeBetween(Integer value1, Integer value2) {
      addCriterion("userOperLog.oper_type between", value1, value2, "operType");
      return (Criteria) this;
    }

    public Criteria andOperTypeNotBetween(Integer value1, Integer value2) {
      addCriterion("userOperLog.oper_type not between", value1, value2, "operType");
      return (Criteria) this;
    }

    public Criteria andOperCodeIsNull() {
      addCriterion("userOperLog.oper_code is null");
      return (Criteria) this;
    }

    public Criteria andOperCodeIsNotNull() {
      addCriterion("userOperLog.oper_code is not null");
      return (Criteria) this;
    }

    public Criteria andOperCodeEqualTo(String value) {
      addCriterion("userOperLog.oper_code =", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeNotEqualTo(String value) {
      addCriterion("userOperLog.oper_code <>", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeGreaterThan(String value) {
      addCriterion("userOperLog.oper_code >", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.oper_code >=", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeLessThan(String value) {
      addCriterion("userOperLog.oper_code <", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.oper_code <=", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeLike(String value) {
      addCriterion("userOperLog.oper_code like", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeNotLike(String value) {
      addCriterion("userOperLog.oper_code not like", value, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeIn(List<String> values) {
      addCriterion("userOperLog.oper_code in", values, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeNotIn(List<String> values) {
      addCriterion("userOperLog.oper_code not in", values, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeBetween(String value1, String value2) {
      addCriterion("userOperLog.oper_code between", value1, value2, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperCodeNotBetween(String value1, String value2) {
      addCriterion("userOperLog.oper_code not between", value1, value2, "operCode");
      return (Criteria) this;
    }

    public Criteria andOperInfoIsNull() {
      addCriterion("userOperLog.oper_info is null");
      return (Criteria) this;
    }

    public Criteria andOperInfoIsNotNull() {
      addCriterion("userOperLog.oper_info is not null");
      return (Criteria) this;
    }

    public Criteria andOperInfoEqualTo(String value) {
      addCriterion("userOperLog.oper_info =", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoNotEqualTo(String value) {
      addCriterion("userOperLog.oper_info <>", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoGreaterThan(String value) {
      addCriterion("userOperLog.oper_info >", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoGreaterThanOrEqualTo(String value) {
      addCriterion("userOperLog.oper_info >=", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoLessThan(String value) {
      addCriterion("userOperLog.oper_info <", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoLessThanOrEqualTo(String value) {
      addCriterion("userOperLog.oper_info <=", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoLike(String value) {
      addCriterion("userOperLog.oper_info like", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoNotLike(String value) {
      addCriterion("userOperLog.oper_info not like", value, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoIn(List<String> values) {
      addCriterion("userOperLog.oper_info in", values, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoNotIn(List<String> values) {
      addCriterion("userOperLog.oper_info not in", values, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoBetween(String value1, String value2) {
      addCriterion("userOperLog.oper_info between", value1, value2, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperInfoNotBetween(String value1, String value2) {
      addCriterion("userOperLog.oper_info not between", value1, value2, "operInfo");
      return (Criteria) this;
    }

    public Criteria andOperTimeIsNull() {
      addCriterion("userOperLog.oper_time is null");
      return (Criteria) this;
    }

    public Criteria andOperTimeIsNotNull() {
      addCriterion("userOperLog.oper_time is not null");
      return (Criteria) this;
    }

    public Criteria andOperTimeEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.oper_time =", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeNotEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.oper_time <>", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeGreaterThan(LocalDateTime value) {
      addCriterion("userOperLog.oper_time >", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.oper_time >=", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeLessThan(LocalDateTime value) {
      addCriterion("userOperLog.oper_time <", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.oper_time <=", value, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeIn(List<LocalDateTime> values) {
      addCriterion("userOperLog.oper_time in", values, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeNotIn(List<LocalDateTime> values) {
      addCriterion("userOperLog.oper_time not in", values, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userOperLog.oper_time between", value1, value2, "operTime");
      return (Criteria) this;
    }

    public Criteria andOperTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userOperLog.oper_time not between", value1, value2, "operTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("userOperLog.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("userOperLog.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("userOperLog.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("userOperLog.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("userOperLog.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("userOperLog.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("userOperLog.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userOperLog.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userOperLog.create_time not between", value1, value2, "createTime");
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