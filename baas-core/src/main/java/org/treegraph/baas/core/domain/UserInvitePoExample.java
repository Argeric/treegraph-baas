package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserInvitePoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public UserInvitePoExample() {
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
      addCriterion("userInvite.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("userInvite.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("userInvite.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("userInvite.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("userInvite.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("userInvite.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("userInvite.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("userInvite.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("userInvite.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("userInvite.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("userInvite.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("userInvite.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andInviterIsNull() {
      addCriterion("userInvite.inviter is null");
      return (Criteria) this;
    }

    public Criteria andInviterIsNotNull() {
      addCriterion("userInvite.inviter is not null");
      return (Criteria) this;
    }

    public Criteria andInviterEqualTo(String value) {
      addCriterion("userInvite.inviter =", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterNotEqualTo(String value) {
      addCriterion("userInvite.inviter <>", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterGreaterThan(String value) {
      addCriterion("userInvite.inviter >", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterGreaterThanOrEqualTo(String value) {
      addCriterion("userInvite.inviter >=", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterLessThan(String value) {
      addCriterion("userInvite.inviter <", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterLessThanOrEqualTo(String value) {
      addCriterion("userInvite.inviter <=", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterLike(String value) {
      addCriterion("userInvite.inviter like", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterNotLike(String value) {
      addCriterion("userInvite.inviter not like", value, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterIn(List<String> values) {
      addCriterion("userInvite.inviter in", values, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterNotIn(List<String> values) {
      addCriterion("userInvite.inviter not in", values, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterBetween(String value1, String value2) {
      addCriterion("userInvite.inviter between", value1, value2, "inviter");
      return (Criteria) this;
    }

    public Criteria andInviterNotBetween(String value1, String value2) {
      addCriterion("userInvite.inviter not between", value1, value2, "inviter");
      return (Criteria) this;
    }

    public Criteria andAccepterIsNull() {
      addCriterion("userInvite.accepter is null");
      return (Criteria) this;
    }

    public Criteria andAccepterIsNotNull() {
      addCriterion("userInvite.accepter is not null");
      return (Criteria) this;
    }

    public Criteria andAccepterEqualTo(String value) {
      addCriterion("userInvite.accepter =", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterNotEqualTo(String value) {
      addCriterion("userInvite.accepter <>", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterGreaterThan(String value) {
      addCriterion("userInvite.accepter >", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterGreaterThanOrEqualTo(String value) {
      addCriterion("userInvite.accepter >=", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterLessThan(String value) {
      addCriterion("userInvite.accepter <", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterLessThanOrEqualTo(String value) {
      addCriterion("userInvite.accepter <=", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterLike(String value) {
      addCriterion("userInvite.accepter like", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterNotLike(String value) {
      addCriterion("userInvite.accepter not like", value, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterIn(List<String> values) {
      addCriterion("userInvite.accepter in", values, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterNotIn(List<String> values) {
      addCriterion("userInvite.accepter not in", values, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterBetween(String value1, String value2) {
      addCriterion("userInvite.accepter between", value1, value2, "accepter");
      return (Criteria) this;
    }

    public Criteria andAccepterNotBetween(String value1, String value2) {
      addCriterion("userInvite.accepter not between", value1, value2, "accepter");
      return (Criteria) this;
    }

    public Criteria andTokenIsNull() {
      addCriterion("userInvite.token is null");
      return (Criteria) this;
    }

    public Criteria andTokenIsNotNull() {
      addCriterion("userInvite.token is not null");
      return (Criteria) this;
    }

    public Criteria andTokenEqualTo(String value) {
      addCriterion("userInvite.token =", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenNotEqualTo(String value) {
      addCriterion("userInvite.token <>", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenGreaterThan(String value) {
      addCriterion("userInvite.token >", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenGreaterThanOrEqualTo(String value) {
      addCriterion("userInvite.token >=", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenLessThan(String value) {
      addCriterion("userInvite.token <", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenLessThanOrEqualTo(String value) {
      addCriterion("userInvite.token <=", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenLike(String value) {
      addCriterion("userInvite.token like", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenNotLike(String value) {
      addCriterion("userInvite.token not like", value, "token");
      return (Criteria) this;
    }

    public Criteria andTokenIn(List<String> values) {
      addCriterion("userInvite.token in", values, "token");
      return (Criteria) this;
    }

    public Criteria andTokenNotIn(List<String> values) {
      addCriterion("userInvite.token not in", values, "token");
      return (Criteria) this;
    }

    public Criteria andTokenBetween(String value1, String value2) {
      addCriterion("userInvite.token between", value1, value2, "token");
      return (Criteria) this;
    }

    public Criteria andTokenNotBetween(String value1, String value2) {
      addCriterion("userInvite.token not between", value1, value2, "token");
      return (Criteria) this;
    }

    public Criteria andExpireIsNull() {
      addCriterion("userInvite.expire is null");
      return (Criteria) this;
    }

    public Criteria andExpireIsNotNull() {
      addCriterion("userInvite.expire is not null");
      return (Criteria) this;
    }

    public Criteria andExpireEqualTo(Integer value) {
      addCriterion("userInvite.expire =", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireNotEqualTo(Integer value) {
      addCriterion("userInvite.expire <>", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireGreaterThan(Integer value) {
      addCriterion("userInvite.expire >", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireGreaterThanOrEqualTo(Integer value) {
      addCriterion("userInvite.expire >=", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireLessThan(Integer value) {
      addCriterion("userInvite.expire <", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireLessThanOrEqualTo(Integer value) {
      addCriterion("userInvite.expire <=", value, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireIn(List<Integer> values) {
      addCriterion("userInvite.expire in", values, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireNotIn(List<Integer> values) {
      addCriterion("userInvite.expire not in", values, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireBetween(Integer value1, Integer value2) {
      addCriterion("userInvite.expire between", value1, value2, "expire");
      return (Criteria) this;
    }

    public Criteria andExpireNotBetween(Integer value1, Integer value2) {
      addCriterion("userInvite.expire not between", value1, value2, "expire");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("userInvite.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("userInvite.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("userInvite.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("userInvite.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("userInvite.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("userInvite.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("userInvite.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("userInvite.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeIsNull() {
      addCriterion("userInvite.invite_time is null");
      return (Criteria) this;
    }

    public Criteria andInviteTimeIsNotNull() {
      addCriterion("userInvite.invite_time is not null");
      return (Criteria) this;
    }

    public Criteria andInviteTimeEqualTo(LocalDateTime value) {
      addCriterion("userInvite.invite_time =", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeNotEqualTo(LocalDateTime value) {
      addCriterion("userInvite.invite_time <>", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeGreaterThan(LocalDateTime value) {
      addCriterion("userInvite.invite_time >", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.invite_time >=", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeLessThan(LocalDateTime value) {
      addCriterion("userInvite.invite_time <", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.invite_time <=", value, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeIn(List<LocalDateTime> values) {
      addCriterion("userInvite.invite_time in", values, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeNotIn(List<LocalDateTime> values) {
      addCriterion("userInvite.invite_time not in", values, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.invite_time between", value1, value2, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andInviteTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.invite_time not between", value1, value2, "inviteTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeIsNull() {
      addCriterion("userInvite.accept_time is null");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeIsNotNull() {
      addCriterion("userInvite.accept_time is not null");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeEqualTo(LocalDateTime value) {
      addCriterion("userInvite.accept_time =", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeNotEqualTo(LocalDateTime value) {
      addCriterion("userInvite.accept_time <>", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeGreaterThan(LocalDateTime value) {
      addCriterion("userInvite.accept_time >", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.accept_time >=", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeLessThan(LocalDateTime value) {
      addCriterion("userInvite.accept_time <", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("userInvite.accept_time <=", value, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeIn(List<LocalDateTime> values) {
      addCriterion("userInvite.accept_time in", values, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeNotIn(List<LocalDateTime> values) {
      addCriterion("userInvite.accept_time not in", values, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.accept_time between", value1, value2, "acceptTime");
      return (Criteria) this;
    }

    public Criteria andAcceptTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("userInvite.accept_time not between", value1, value2, "acceptTime");
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