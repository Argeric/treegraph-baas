package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsortiumPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public ConsortiumPoExample() {
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

    public Criteria andConsortiumIdIsNull() {
      addCriterion("consortium.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("consortium.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("consortium.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("consortium.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("consortium.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("consortium.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("consortium.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("consortium.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("consortium.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("consortium.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("consortium.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("consortium.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("consortium.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andNameIsNull() {
      addCriterion("consortium.`name` is null");
      return (Criteria) this;
    }

    public Criteria andNameIsNotNull() {
      addCriterion("consortium.`name` is not null");
      return (Criteria) this;
    }

    public Criteria andNameEqualTo(String value) {
      addCriterion("consortium.`name` =", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameNotEqualTo(String value) {
      addCriterion("consortium.`name` <>", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameGreaterThan(String value) {
      addCriterion("consortium.`name` >", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.`name` >=", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameLessThan(String value) {
      addCriterion("consortium.`name` <", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameLessThanOrEqualTo(String value) {
      addCriterion("consortium.`name` <=", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameLike(String value) {
      addCriterion("consortium.`name` like", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameNotLike(String value) {
      addCriterion("consortium.`name` not like", value, "name");
      return (Criteria) this;
    }

    public Criteria andNameIn(List<String> values) {
      addCriterion("consortium.`name` in", values, "name");
      return (Criteria) this;
    }

    public Criteria andNameNotIn(List<String> values) {
      addCriterion("consortium.`name` not in", values, "name");
      return (Criteria) this;
    }

    public Criteria andNameBetween(String value1, String value2) {
      addCriterion("consortium.`name` between", value1, value2, "name");
      return (Criteria) this;
    }

    public Criteria andNameNotBetween(String value1, String value2) {
      addCriterion("consortium.`name` not between", value1, value2, "name");
      return (Criteria) this;
    }

    public Criteria andDescriptionIsNull() {
      addCriterion("consortium.description is null");
      return (Criteria) this;
    }

    public Criteria andDescriptionIsNotNull() {
      addCriterion("consortium.description is not null");
      return (Criteria) this;
    }

    public Criteria andDescriptionEqualTo(String value) {
      addCriterion("consortium.description =", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionNotEqualTo(String value) {
      addCriterion("consortium.description <>", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionGreaterThan(String value) {
      addCriterion("consortium.description >", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.description >=", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionLessThan(String value) {
      addCriterion("consortium.description <", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionLessThanOrEqualTo(String value) {
      addCriterion("consortium.description <=", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionLike(String value) {
      addCriterion("consortium.description like", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionNotLike(String value) {
      addCriterion("consortium.description not like", value, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionIn(List<String> values) {
      addCriterion("consortium.description in", values, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionNotIn(List<String> values) {
      addCriterion("consortium.description not in", values, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionBetween(String value1, String value2) {
      addCriterion("consortium.description between", value1, value2, "description");
      return (Criteria) this;
    }

    public Criteria andDescriptionNotBetween(String value1, String value2) {
      addCriterion("consortium.description not between", value1, value2, "description");
      return (Criteria) this;
    }

    public Criteria andContactNameIsNull() {
      addCriterion("consortium.contact_name is null");
      return (Criteria) this;
    }

    public Criteria andContactNameIsNotNull() {
      addCriterion("consortium.contact_name is not null");
      return (Criteria) this;
    }

    public Criteria andContactNameEqualTo(String value) {
      addCriterion("consortium.contact_name =", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameNotEqualTo(String value) {
      addCriterion("consortium.contact_name <>", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameGreaterThan(String value) {
      addCriterion("consortium.contact_name >", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.contact_name >=", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameLessThan(String value) {
      addCriterion("consortium.contact_name <", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameLessThanOrEqualTo(String value) {
      addCriterion("consortium.contact_name <=", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameLike(String value) {
      addCriterion("consortium.contact_name like", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameNotLike(String value) {
      addCriterion("consortium.contact_name not like", value, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameIn(List<String> values) {
      addCriterion("consortium.contact_name in", values, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameNotIn(List<String> values) {
      addCriterion("consortium.contact_name not in", values, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameBetween(String value1, String value2) {
      addCriterion("consortium.contact_name between", value1, value2, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactNameNotBetween(String value1, String value2) {
      addCriterion("consortium.contact_name not between", value1, value2, "contactName");
      return (Criteria) this;
    }

    public Criteria andContactEmailIsNull() {
      addCriterion("consortium.contact_email is null");
      return (Criteria) this;
    }

    public Criteria andContactEmailIsNotNull() {
      addCriterion("consortium.contact_email is not null");
      return (Criteria) this;
    }

    public Criteria andContactEmailEqualTo(String value) {
      addCriterion("consortium.contact_email =", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailNotEqualTo(String value) {
      addCriterion("consortium.contact_email <>", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailGreaterThan(String value) {
      addCriterion("consortium.contact_email >", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.contact_email >=", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailLessThan(String value) {
      addCriterion("consortium.contact_email <", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailLessThanOrEqualTo(String value) {
      addCriterion("consortium.contact_email <=", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailLike(String value) {
      addCriterion("consortium.contact_email like", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailNotLike(String value) {
      addCriterion("consortium.contact_email not like", value, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailIn(List<String> values) {
      addCriterion("consortium.contact_email in", values, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailNotIn(List<String> values) {
      addCriterion("consortium.contact_email not in", values, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailBetween(String value1, String value2) {
      addCriterion("consortium.contact_email between", value1, value2, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactEmailNotBetween(String value1, String value2) {
      addCriterion("consortium.contact_email not between", value1, value2, "contactEmail");
      return (Criteria) this;
    }

    public Criteria andContactPhoneIsNull() {
      addCriterion("consortium.contact_phone is null");
      return (Criteria) this;
    }

    public Criteria andContactPhoneIsNotNull() {
      addCriterion("consortium.contact_phone is not null");
      return (Criteria) this;
    }

    public Criteria andContactPhoneEqualTo(String value) {
      addCriterion("consortium.contact_phone =", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneNotEqualTo(String value) {
      addCriterion("consortium.contact_phone <>", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneGreaterThan(String value) {
      addCriterion("consortium.contact_phone >", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.contact_phone >=", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneLessThan(String value) {
      addCriterion("consortium.contact_phone <", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneLessThanOrEqualTo(String value) {
      addCriterion("consortium.contact_phone <=", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneLike(String value) {
      addCriterion("consortium.contact_phone like", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneNotLike(String value) {
      addCriterion("consortium.contact_phone not like", value, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneIn(List<String> values) {
      addCriterion("consortium.contact_phone in", values, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneNotIn(List<String> values) {
      addCriterion("consortium.contact_phone not in", values, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneBetween(String value1, String value2) {
      addCriterion("consortium.contact_phone between", value1, value2, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andContactPhoneNotBetween(String value1, String value2) {
      addCriterion("consortium.contact_phone not between", value1, value2, "contactPhone");
      return (Criteria) this;
    }

    public Criteria andStatusIsNull() {
      addCriterion("consortium.`status` is null");
      return (Criteria) this;
    }

    public Criteria andStatusIsNotNull() {
      addCriterion("consortium.`status` is not null");
      return (Criteria) this;
    }

    public Criteria andStatusEqualTo(Integer value) {
      addCriterion("consortium.`status` =", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotEqualTo(Integer value) {
      addCriterion("consortium.`status` <>", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThan(Integer value) {
      addCriterion("consortium.`status` >", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("consortium.`status` >=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThan(Integer value) {
      addCriterion("consortium.`status` <", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThanOrEqualTo(Integer value) {
      addCriterion("consortium.`status` <=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusIn(List<Integer> values) {
      addCriterion("consortium.`status` in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotIn(List<Integer> values) {
      addCriterion("consortium.`status` not in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusBetween(Integer value1, Integer value2) {
      addCriterion("consortium.`status` between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("consortium.`status` not between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andEstablishStepIsNull() {
      addCriterion("consortium.establish_step is null");
      return (Criteria) this;
    }

    public Criteria andEstablishStepIsNotNull() {
      addCriterion("consortium.establish_step is not null");
      return (Criteria) this;
    }

    public Criteria andEstablishStepEqualTo(Integer value) {
      addCriterion("consortium.establish_step =", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepNotEqualTo(Integer value) {
      addCriterion("consortium.establish_step <>", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepGreaterThan(Integer value) {
      addCriterion("consortium.establish_step >", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepGreaterThanOrEqualTo(Integer value) {
      addCriterion("consortium.establish_step >=", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepLessThan(Integer value) {
      addCriterion("consortium.establish_step <", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepLessThanOrEqualTo(Integer value) {
      addCriterion("consortium.establish_step <=", value, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepIn(List<Integer> values) {
      addCriterion("consortium.establish_step in", values, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepNotIn(List<Integer> values) {
      addCriterion("consortium.establish_step not in", values, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepBetween(Integer value1, Integer value2) {
      addCriterion("consortium.establish_step between", value1, value2, "establishStep");
      return (Criteria) this;
    }

    public Criteria andEstablishStepNotBetween(Integer value1, Integer value2) {
      addCriterion("consortium.establish_step not between", value1, value2, "establishStep");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("consortium.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("consortium.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("consortium.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("consortium.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("consortium.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("consortium.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("consortium.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("consortium.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("consortium.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("consortium.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("consortium.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("consortium.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("consortium.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("consortium.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("consortium.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("consortium.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("consortium.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("consortium.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("consortium.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("consortium.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("consortium.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("consortium.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("consortium.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("consortium.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("consortium.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("consortium.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("consortium.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("consortium.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("consortium.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("consortium.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("consortium.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("consortium.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("consortium.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("consortium.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("consortium.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("consortium.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("consortium.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("consortium.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("consortium.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("consortium.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("consortium.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("consortium.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("consortium.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("consortium.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("consortium.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("consortium.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("consortium.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("consortium.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("consortium.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("consortium.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("consortium.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("consortium.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("consortium.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("consortium.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("consortium.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("consortium.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("consortium.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("consortium.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("consortium.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("consortium.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("consortium.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("consortium.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("consortium.update_by not between", value1, value2, "updateBy");
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