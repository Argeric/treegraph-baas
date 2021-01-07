package org.treegraph.baas.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KeypairPoExample {

  protected String orderByClause;

  protected boolean distinct;

  protected List<Criteria> oredCriteria;

  public KeypairPoExample() {
    oredCriteria = new ArrayList<Criteria>();
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
      criteria = new ArrayList<Criterion>();
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
      addCriterion("keypair.id is null");
      return (Criteria) this;
    }

    public Criteria andIdIsNotNull() {
      addCriterion("keypair.id is not null");
      return (Criteria) this;
    }

    public Criteria andIdEqualTo(Long value) {
      addCriterion("keypair.id =", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotEqualTo(Long value) {
      addCriterion("keypair.id <>", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThan(Long value) {
      addCriterion("keypair.id >", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdGreaterThanOrEqualTo(Long value) {
      addCriterion("keypair.id >=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThan(Long value) {
      addCriterion("keypair.id <", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdLessThanOrEqualTo(Long value) {
      addCriterion("keypair.id <=", value, "id");
      return (Criteria) this;
    }

    public Criteria andIdIn(List<Long> values) {
      addCriterion("keypair.id in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotIn(List<Long> values) {
      addCriterion("keypair.id not in", values, "id");
      return (Criteria) this;
    }

    public Criteria andIdBetween(Long value1, Long value2) {
      addCriterion("keypair.id between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andIdNotBetween(Long value1, Long value2) {
      addCriterion("keypair.id not between", value1, value2, "id");
      return (Criteria) this;
    }

    public Criteria andPublicKeyIsNull() {
      addCriterion("keypair.public_key is null");
      return (Criteria) this;
    }

    public Criteria andPublicKeyIsNotNull() {
      addCriterion("keypair.public_key is not null");
      return (Criteria) this;
    }

    public Criteria andPublicKeyEqualTo(String value) {
      addCriterion("keypair.public_key =", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyNotEqualTo(String value) {
      addCriterion("keypair.public_key <>", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyGreaterThan(String value) {
      addCriterion("keypair.public_key >", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.public_key >=", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyLessThan(String value) {
      addCriterion("keypair.public_key <", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyLessThanOrEqualTo(String value) {
      addCriterion("keypair.public_key <=", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyLike(String value) {
      addCriterion("keypair.public_key like", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyNotLike(String value) {
      addCriterion("keypair.public_key not like", value, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyIn(List<String> values) {
      addCriterion("keypair.public_key in", values, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyNotIn(List<String> values) {
      addCriterion("keypair.public_key not in", values, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyBetween(String value1, String value2) {
      addCriterion("keypair.public_key between", value1, value2, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPublicKeyNotBetween(String value1, String value2) {
      addCriterion("keypair.public_key not between", value1, value2, "publicKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyIsNull() {
      addCriterion("keypair.private_key is null");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyIsNotNull() {
      addCriterion("keypair.private_key is not null");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyEqualTo(String value) {
      addCriterion("keypair.private_key =", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyNotEqualTo(String value) {
      addCriterion("keypair.private_key <>", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyGreaterThan(String value) {
      addCriterion("keypair.private_key >", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.private_key >=", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyLessThan(String value) {
      addCriterion("keypair.private_key <", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyLessThanOrEqualTo(String value) {
      addCriterion("keypair.private_key <=", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyLike(String value) {
      addCriterion("keypair.private_key like", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyNotLike(String value) {
      addCriterion("keypair.private_key not like", value, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyIn(List<String> values) {
      addCriterion("keypair.private_key in", values, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyNotIn(List<String> values) {
      addCriterion("keypair.private_key not in", values, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyBetween(String value1, String value2) {
      addCriterion("keypair.private_key between", value1, value2, "privateKey");
      return (Criteria) this;
    }

    public Criteria andPrivateKeyNotBetween(String value1, String value2) {
      addCriterion("keypair.private_key not between", value1, value2, "privateKey");
      return (Criteria) this;
    }

    public Criteria andCsrStrIsNull() {
      addCriterion("keypair.csr_str is null");
      return (Criteria) this;
    }

    public Criteria andCsrStrIsNotNull() {
      addCriterion("keypair.csr_str is not null");
      return (Criteria) this;
    }

    public Criteria andCsrStrEqualTo(String value) {
      addCriterion("keypair.csr_str =", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrNotEqualTo(String value) {
      addCriterion("keypair.csr_str <>", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrGreaterThan(String value) {
      addCriterion("keypair.csr_str >", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.csr_str >=", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrLessThan(String value) {
      addCriterion("keypair.csr_str <", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrLessThanOrEqualTo(String value) {
      addCriterion("keypair.csr_str <=", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrLike(String value) {
      addCriterion("keypair.csr_str like", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrNotLike(String value) {
      addCriterion("keypair.csr_str not like", value, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrIn(List<String> values) {
      addCriterion("keypair.csr_str in", values, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrNotIn(List<String> values) {
      addCriterion("keypair.csr_str not in", values, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrBetween(String value1, String value2) {
      addCriterion("keypair.csr_str between", value1, value2, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCsrStrNotBetween(String value1, String value2) {
      addCriterion("keypair.csr_str not between", value1, value2, "csrStr");
      return (Criteria) this;
    }

    public Criteria andCertStrIsNull() {
      addCriterion("keypair.cert_str is null");
      return (Criteria) this;
    }

    public Criteria andCertStrIsNotNull() {
      addCriterion("keypair.cert_str is not null");
      return (Criteria) this;
    }

    public Criteria andCertStrEqualTo(String value) {
      addCriterion("keypair.cert_str =", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrNotEqualTo(String value) {
      addCriterion("keypair.cert_str <>", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrGreaterThan(String value) {
      addCriterion("keypair.cert_str >", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.cert_str >=", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrLessThan(String value) {
      addCriterion("keypair.cert_str <", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrLessThanOrEqualTo(String value) {
      addCriterion("keypair.cert_str <=", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrLike(String value) {
      addCriterion("keypair.cert_str like", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrNotLike(String value) {
      addCriterion("keypair.cert_str not like", value, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrIn(List<String> values) {
      addCriterion("keypair.cert_str in", values, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrNotIn(List<String> values) {
      addCriterion("keypair.cert_str not in", values, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrBetween(String value1, String value2) {
      addCriterion("keypair.cert_str between", value1, value2, "certStr");
      return (Criteria) this;
    }

    public Criteria andCertStrNotBetween(String value1, String value2) {
      addCriterion("keypair.cert_str not between", value1, value2, "certStr");
      return (Criteria) this;
    }

    public Criteria andAlgorithmIsNull() {
      addCriterion("keypair.algorithm is null");
      return (Criteria) this;
    }

    public Criteria andAlgorithmIsNotNull() {
      addCriterion("keypair.algorithm is not null");
      return (Criteria) this;
    }

    public Criteria andAlgorithmEqualTo(String value) {
      addCriterion("keypair.algorithm =", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmNotEqualTo(String value) {
      addCriterion("keypair.algorithm <>", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmGreaterThan(String value) {
      addCriterion("keypair.algorithm >", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.algorithm >=", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmLessThan(String value) {
      addCriterion("keypair.algorithm <", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmLessThanOrEqualTo(String value) {
      addCriterion("keypair.algorithm <=", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmLike(String value) {
      addCriterion("keypair.algorithm like", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmNotLike(String value) {
      addCriterion("keypair.algorithm not like", value, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmIn(List<String> values) {
      addCriterion("keypair.algorithm in", values, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmNotIn(List<String> values) {
      addCriterion("keypair.algorithm not in", values, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmBetween(String value1, String value2) {
      addCriterion("keypair.algorithm between", value1, value2, "algorithm");
      return (Criteria) this;
    }

    public Criteria andAlgorithmNotBetween(String value1, String value2) {
      addCriterion("keypair.algorithm not between", value1, value2, "algorithm");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNull() {
      addCriterion("keypair.consortium_id is null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIsNotNull() {
      addCriterion("keypair.consortium_id is not null");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdEqualTo(String value) {
      addCriterion("keypair.consortium_id =", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotEqualTo(String value) {
      addCriterion("keypair.consortium_id <>", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThan(String value) {
      addCriterion("keypair.consortium_id >", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.consortium_id >=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThan(String value) {
      addCriterion("keypair.consortium_id <", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
      addCriterion("keypair.consortium_id <=", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdLike(String value) {
      addCriterion("keypair.consortium_id like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotLike(String value) {
      addCriterion("keypair.consortium_id not like", value, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdIn(List<String> values) {
      addCriterion("keypair.consortium_id in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotIn(List<String> values) {
      addCriterion("keypair.consortium_id not in", values, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdBetween(String value1, String value2) {
      addCriterion("keypair.consortium_id between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andConsortiumIdNotBetween(String value1, String value2) {
      addCriterion("keypair.consortium_id not between", value1, value2, "consortiumId");
      return (Criteria) this;
    }

    public Criteria andKeyTypeIsNull() {
      addCriterion("keypair.`key_type` is null");
      return (Criteria) this;
    }

    public Criteria andKeyTypeIsNotNull() {
      addCriterion("keypair.`key_type` is not null");
      return (Criteria) this;
    }

    public Criteria andKeyTypeEqualTo(Integer value) {
      addCriterion("keypair.`key_type` =", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeNotEqualTo(Integer value) {
      addCriterion("keypair.`key_type` <>", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeGreaterThan(Integer value) {
      addCriterion("keypair.`key_type` >", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeGreaterThanOrEqualTo(Integer value) {
      addCriterion("keypair.`key_type` >=", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeLessThan(Integer value) {
      addCriterion("keypair.`key_type` <", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeLessThanOrEqualTo(Integer value) {
      addCriterion("keypair.`key_type` <=", value, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeIn(List<Integer> values) {
      addCriterion("keypair.`key_type` in", values, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeNotIn(List<Integer> values) {
      addCriterion("keypair.`key_type` not in", values, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeBetween(Integer value1, Integer value2) {
      addCriterion("keypair.`key_type` between", value1, value2, "keyType");
      return (Criteria) this;
    }

    public Criteria andKeyTypeNotBetween(Integer value1, Integer value2) {
      addCriterion("keypair.`key_type` not between", value1, value2, "keyType");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNull() {
      addCriterion("keypair.delete_flag is null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIsNotNull() {
      addCriterion("keypair.delete_flag is not null");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagEqualTo(Integer value) {
      addCriterion("keypair.delete_flag =", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotEqualTo(Integer value) {
      addCriterion("keypair.delete_flag <>", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThan(Integer value) {
      addCriterion("keypair.delete_flag >", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
      addCriterion("keypair.delete_flag >=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThan(Integer value) {
      addCriterion("keypair.delete_flag <", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
      addCriterion("keypair.delete_flag <=", value, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagIn(List<Integer> values) {
      addCriterion("keypair.delete_flag in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotIn(List<Integer> values) {
      addCriterion("keypair.delete_flag not in", values, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
      addCriterion("keypair.delete_flag between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
      addCriterion("keypair.delete_flag not between", value1, value2, "deleteFlag");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNull() {
      addCriterion("keypair.create_time is null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIsNotNull() {
      addCriterion("keypair.create_time is not null");
      return (Criteria) this;
    }

    public Criteria andCreateTimeEqualTo(Date value) {
      addCriterion("keypair.create_time =", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotEqualTo(Date value) {
      addCriterion("keypair.create_time <>", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThan(Date value) {
      addCriterion("keypair.create_time >", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
      addCriterion("keypair.create_time >=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThan(Date value) {
      addCriterion("keypair.create_time <", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
      addCriterion("keypair.create_time <=", value, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeIn(List<Date> values) {
      addCriterion("keypair.create_time in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotIn(List<Date> values) {
      addCriterion("keypair.create_time not in", values, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeBetween(Date value1, Date value2) {
      addCriterion("keypair.create_time between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
      addCriterion("keypair.create_time not between", value1, value2, "createTime");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNull() {
      addCriterion("keypair.create_by is null");
      return (Criteria) this;
    }

    public Criteria andCreateByIsNotNull() {
      addCriterion("keypair.create_by is not null");
      return (Criteria) this;
    }

    public Criteria andCreateByEqualTo(String value) {
      addCriterion("keypair.create_by =", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotEqualTo(String value) {
      addCriterion("keypair.create_by <>", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThan(String value) {
      addCriterion("keypair.create_by >", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.create_by >=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThan(String value) {
      addCriterion("keypair.create_by <", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLessThanOrEqualTo(String value) {
      addCriterion("keypair.create_by <=", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByLike(String value) {
      addCriterion("keypair.create_by like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotLike(String value) {
      addCriterion("keypair.create_by not like", value, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByIn(List<String> values) {
      addCriterion("keypair.create_by in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotIn(List<String> values) {
      addCriterion("keypair.create_by not in", values, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByBetween(String value1, String value2) {
      addCriterion("keypair.create_by between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andCreateByNotBetween(String value1, String value2) {
      addCriterion("keypair.create_by not between", value1, value2, "createBy");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNull() {
      addCriterion("keypair.update_time is null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIsNotNull() {
      addCriterion("keypair.update_time is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeEqualTo(Date value) {
      addCriterion("keypair.update_time =", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotEqualTo(Date value) {
      addCriterion("keypair.update_time <>", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThan(Date value) {
      addCriterion("keypair.update_time >", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
      addCriterion("keypair.update_time >=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThan(Date value) {
      addCriterion("keypair.update_time <", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
      addCriterion("keypair.update_time <=", value, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeIn(List<Date> values) {
      addCriterion("keypair.update_time in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotIn(List<Date> values) {
      addCriterion("keypair.update_time not in", values, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeBetween(Date value1, Date value2) {
      addCriterion("keypair.update_time between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
      addCriterion("keypair.update_time not between", value1, value2, "updateTime");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNull() {
      addCriterion("keypair.update_by is null");
      return (Criteria) this;
    }

    public Criteria andUpdateByIsNotNull() {
      addCriterion("keypair.update_by is not null");
      return (Criteria) this;
    }

    public Criteria andUpdateByEqualTo(String value) {
      addCriterion("keypair.update_by =", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotEqualTo(String value) {
      addCriterion("keypair.update_by <>", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThan(String value) {
      addCriterion("keypair.update_by >", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
      addCriterion("keypair.update_by >=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThan(String value) {
      addCriterion("keypair.update_by <", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLessThanOrEqualTo(String value) {
      addCriterion("keypair.update_by <=", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByLike(String value) {
      addCriterion("keypair.update_by like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotLike(String value) {
      addCriterion("keypair.update_by not like", value, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByIn(List<String> values) {
      addCriterion("keypair.update_by in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotIn(List<String> values) {
      addCriterion("keypair.update_by not in", values, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByBetween(String value1, String value2) {
      addCriterion("keypair.update_by between", value1, value2, "updateBy");
      return (Criteria) this;
    }

    public Criteria andUpdateByNotBetween(String value1, String value2) {
      addCriterion("keypair.update_by not between", value1, value2, "updateBy");
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