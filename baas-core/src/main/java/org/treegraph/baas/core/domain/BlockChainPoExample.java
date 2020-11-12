package org.treegraph.baas.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockChainPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public BlockChainPoExample() {
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
      addCriterion("blockChain.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("blockChain.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(String value) {
      addCriterion("blockChain.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(String value) {
      addCriterion("blockChain.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(String value) {
      addCriterion("blockChain.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(String value) {
      addCriterion("blockChain.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(String value) {
      addCriterion("blockChain.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLike(String value) {
      addCriterion("blockChain.id like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotLike(String value) {
      addCriterion("blockChain.id not like", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<String> values) {
      addCriterion("blockChain.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<String> values) {
      addCriterion("blockChain.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(String value1, String value2) {
      addCriterion("blockChain.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(String value1, String value2) {
      addCriterion("blockChain.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("blockChain.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("blockChain.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("blockChain.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("blockChain.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("blockChain.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("blockChain.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("blockChain.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("blockChain.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("blockChain.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("blockChain.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("blockChain.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("blockChain.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("blockChain.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameIsNull() {
      addCriterion("blockChain.blockchain_name is null");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameIsNotNull() {
      addCriterion("blockChain.blockchain_name is not null");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameEqualTo(String value) {
      addCriterion("blockChain.blockchain_name =", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameNotEqualTo(String value) {
      addCriterion("blockChain.blockchain_name <>", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameGreaterThan(String value) {
      addCriterion("blockChain.blockchain_name >", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.blockchain_name >=", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameLessThan(String value) {
      addCriterion("blockChain.blockchain_name <", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameLessThanOrEqualTo(String value) {
      addCriterion("blockChain.blockchain_name <=", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameLike(String value) {
      addCriterion("blockChain.blockchain_name like", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameNotLike(String value) {
      addCriterion("blockChain.blockchain_name not like", value, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameIn(List<String> values) {
      addCriterion("blockChain.blockchain_name in", values, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameNotIn(List<String> values) {
      addCriterion("blockChain.blockchain_name not in", values, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameBetween(String value1, String value2) {
      addCriterion("blockChain.blockchain_name between", value1, value2, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andBlockchainNameNotBetween(String value1, String value2) {
      addCriterion("blockChain.blockchain_name not between", value1, value2, "blockchainName");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailIsNull() {
      addCriterion("blockChain.alarm_email is null");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailIsNotNull() {
      addCriterion("blockChain.alarm_email is not null");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailEqualTo(String value) {
      addCriterion("blockChain.alarm_email =", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailNotEqualTo(String value) {
      addCriterion("blockChain.alarm_email <>", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailGreaterThan(String value) {
      addCriterion("blockChain.alarm_email >", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.alarm_email >=", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailLessThan(String value) {
      addCriterion("blockChain.alarm_email <", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailLessThanOrEqualTo(String value) {
      addCriterion("blockChain.alarm_email <=", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailLike(String value) {
      addCriterion("blockChain.alarm_email like", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailNotLike(String value) {
      addCriterion("blockChain.alarm_email not like", value, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailIn(List<String> values) {
      addCriterion("blockChain.alarm_email in", values, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailNotIn(List<String> values) {
      addCriterion("blockChain.alarm_email not in", values, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailBetween(String value1, String value2) {
      addCriterion("blockChain.alarm_email between", value1, value2, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andAlarmEmailNotBetween(String value1, String value2) {
      addCriterion("blockChain.alarm_email not between", value1, value2, "alarmEmail");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmIsNull() {
      addCriterion("blockChain.consensus_algorithm is null");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmIsNotNull() {
      addCriterion("blockChain.consensus_algorithm is not null");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmEqualTo(Integer value) {
      addCriterion("blockChain.consensus_algorithm =", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmNotEqualTo(Integer value) {
      addCriterion("blockChain.consensus_algorithm <>", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmGreaterThan(Integer value) {
      addCriterion("blockChain.consensus_algorithm >", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmGreaterThanOrEqualTo(Integer value) {
      addCriterion("blockChain.consensus_algorithm >=", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmLessThan(Integer value) {
      addCriterion("blockChain.consensus_algorithm <", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmLessThanOrEqualTo(Integer value) {
      addCriterion("blockChain.consensus_algorithm <=", value, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmIn(List<Integer> values) {
      addCriterion("blockChain.consensus_algorithm in", values, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmNotIn(List<Integer> values) {
      addCriterion("blockChain.consensus_algorithm not in", values, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.consensus_algorithm between", value1, value2, "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andConsensusAlgorithmNotBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.consensus_algorithm not between", value1, value2,
          "consensusAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmIsNull() {
      addCriterion("blockChain.encrypt_algorithm is null");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmIsNotNull() {
      addCriterion("blockChain.encrypt_algorithm is not null");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmEqualTo(Integer value) {
      addCriterion("blockChain.encrypt_algorithm =", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmNotEqualTo(Integer value) {
      addCriterion("blockChain.encrypt_algorithm <>", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmGreaterThan(Integer value) {
      addCriterion("blockChain.encrypt_algorithm >", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmGreaterThanOrEqualTo(Integer value) {
      addCriterion("blockChain.encrypt_algorithm >=", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmLessThan(Integer value) {
      addCriterion("blockChain.encrypt_algorithm <", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmLessThanOrEqualTo(Integer value) {
      addCriterion("blockChain.encrypt_algorithm <=", value, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmIn(List<Integer> values) {
      addCriterion("blockChain.encrypt_algorithm in", values, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmNotIn(List<Integer> values) {
      addCriterion("blockChain.encrypt_algorithm not in", values, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.encrypt_algorithm between", value1, value2, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andEncryptAlgorithmNotBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.encrypt_algorithm not between", value1, value2, "encryptAlgorithm");
      return (Criteria) this;
    }

    public Criteria andStatusIsNull() {
      addCriterion("blockChain.`status` is null");
      return (Criteria) this;
    }

    public Criteria andStatusIsNotNull() {
      addCriterion("blockChain.`status` is not null");
      return (Criteria) this;
    }

    public Criteria andStatusEqualTo(Integer value) {
      addCriterion("blockChain.`status` =", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotEqualTo(Integer value) {
      addCriterion("blockChain.`status` <>", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThan(Integer value) {
      addCriterion("blockChain.`status` >", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
      addCriterion("blockChain.`status` >=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThan(Integer value) {
      addCriterion("blockChain.`status` <", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusLessThanOrEqualTo(Integer value) {
      addCriterion("blockChain.`status` <=", value, "status");
      return (Criteria) this;
    }

    public Criteria andStatusIn(List<Integer> values) {
      addCriterion("blockChain.`status` in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotIn(List<Integer> values) {
      addCriterion("blockChain.`status` not in", values, "status");
      return (Criteria) this;
    }

    public Criteria andStatusBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.`status` between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andStatusNotBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.`status` not between", value1, value2, "status");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("blockChain.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("blockChain.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("blockChain.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("blockChain.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("blockChain.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("blockChain.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("blockChain.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("blockChain.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("blockChain.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("blockChain.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("blockChain.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("blockChain.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("blockChain.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(LocalDateTime value) {
      addCriterion("blockChain.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("blockChain.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
      addCriterion("blockChain.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("blockChain.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(LocalDateTime value) {
      addCriterion("blockChain.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("blockChain.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<LocalDateTime> values) {
      addCriterion("blockChain.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("blockChain.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("blockChain.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("blockChain.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("blockChain.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("blockChain.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
      addCriterion("blockChain.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
      addCriterion("blockChain.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
      addCriterion("blockChain.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
      addCriterion("blockChain.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(LocalDateTime value) {
      addCriterion("blockChain.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
      addCriterion("blockChain.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
      addCriterion("blockChain.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
      addCriterion("blockChain.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("blockChain.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
      addCriterion("blockChain.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("blockChain.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("blockChain.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("blockChain.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("blockChain.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("blockChain.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("blockChain.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("blockChain.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("blockChain.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("blockChain.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("blockChain.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("blockChain.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("blockChain.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("blockChain.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("blockChain.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("blockChain.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("blockChain.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("blockChain.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("blockChain.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("blockChain.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("blockChain.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("blockChain.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("blockChain.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("blockChain.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("blockChain.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("blockChain.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("blockChain.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("blockChain.update_by not between", value1, value2, "updateBy");
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