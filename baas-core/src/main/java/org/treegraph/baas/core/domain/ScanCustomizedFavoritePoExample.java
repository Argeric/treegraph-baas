package org.treegraph.baas.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScanCustomizedFavoritePoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ScanCustomizedFavoritePoExample() {
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
            addCriterion("scanCustomizedFavorite.id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("scanCustomizedFavorite.id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("scanCustomizedFavorite.id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("scanCustomizedFavorite.id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("scanCustomizedFavorite.id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("scanCustomizedFavorite.id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("scanCustomizedFavorite.id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("scanCustomizedFavorite.id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("scanCustomizedFavorite.id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("scanCustomizedFavorite.id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("scanCustomizedFavorite.id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("scanCustomizedFavorite.id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("scanCustomizedFavorite.user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("scanCustomizedFavorite.user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("scanCustomizedFavorite.user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("scanCustomizedFavorite.user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("scanCustomizedFavorite.user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIsNull() {
            addCriterion("scanCustomizedFavorite.consortium_id is null");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIsNotNull() {
            addCriterion("scanCustomizedFavorite.consortium_id is not null");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id =", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id <>", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id >", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id >=", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLessThan(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id <", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id <=", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLike(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id like", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotLike(String value) {
            addCriterion("scanCustomizedFavorite.consortium_id not like", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.consortium_id in", values, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.consortium_id not in", values, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.consortium_id between", value1, value2, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.consortium_id not between", value1, value2, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdIsNull() {
            addCriterion("scanCustomizedFavorite.blockchain_id is null");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdIsNotNull() {
            addCriterion("scanCustomizedFavorite.blockchain_id is not null");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id =", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id <>", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id >", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id >=", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdLessThan(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id <", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id <=", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdLike(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id like", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdNotLike(String value) {
            addCriterion("scanCustomizedFavorite.blockchain_id not like", value, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.blockchain_id in", values, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.blockchain_id not in", values, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.blockchain_id between", value1, value2, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andBlockchainIdNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.blockchain_id not between", value1, value2, "blockchainId");
            return (Criteria) this;
        }

        public Criteria andHashIsNull() {
            addCriterion("scanCustomizedFavorite.hash is null");
            return (Criteria) this;
        }

        public Criteria andHashIsNotNull() {
            addCriterion("scanCustomizedFavorite.hash is not null");
            return (Criteria) this;
        }

        public Criteria andHashEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.hash =", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.hash <>", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.hash >", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.hash >=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThan(String value) {
            addCriterion("scanCustomizedFavorite.hash <", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.hash <=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLike(String value) {
            addCriterion("scanCustomizedFavorite.hash like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotLike(String value) {
            addCriterion("scanCustomizedFavorite.hash not like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.hash in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.hash not in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.hash between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.hash not between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("scanCustomizedFavorite.remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("scanCustomizedFavorite.remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("scanCustomizedFavorite.remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("scanCustomizedFavorite.remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("scanCustomizedFavorite.remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIsNull() {
            addCriterion("scanCustomizedFavorite.delete_flag is null");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIsNotNull() {
            addCriterion("scanCustomizedFavorite.delete_flag is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagEqualTo(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag =", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotEqualTo(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag <>", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagGreaterThan(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag >", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag >=", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagLessThan(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag <", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
            addCriterion("scanCustomizedFavorite.delete_flag <=", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIn(List<Integer> values) {
            addCriterion("scanCustomizedFavorite.delete_flag in", values, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotIn(List<Integer> values) {
            addCriterion("scanCustomizedFavorite.delete_flag not in", values, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
            addCriterion("scanCustomizedFavorite.delete_flag between", value1, value2, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("scanCustomizedFavorite.delete_flag not between", value1, value2, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("scanCustomizedFavorite.create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("scanCustomizedFavorite.create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("scanCustomizedFavorite.create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("scanCustomizedFavorite.create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("scanCustomizedFavorite.create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("scanCustomizedFavorite.create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("scanCustomizedFavorite.create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("scanCustomizedFavorite.create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("scanCustomizedFavorite.update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("scanCustomizedFavorite.update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("scanCustomizedFavorite.update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("scanCustomizedFavorite.update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("scanCustomizedFavorite.update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("scanCustomizedFavorite.update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("scanCustomizedFavorite.update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("scanCustomizedFavorite.update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("scanCustomizedFavorite.update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("scanCustomizedFavorite.create_by is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("scanCustomizedFavorite.create_by is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.create_by =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.create_by <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.create_by >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.create_by >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("scanCustomizedFavorite.create_by <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.create_by <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("scanCustomizedFavorite.create_by like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("scanCustomizedFavorite.create_by not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.create_by in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.create_by not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.create_by between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.create_by not between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNull() {
            addCriterion("scanCustomizedFavorite.update_by is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("scanCustomizedFavorite.update_by is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.update_by =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.update_by <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("scanCustomizedFavorite.update_by >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.update_by >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("scanCustomizedFavorite.update_by <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("scanCustomizedFavorite.update_by <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("scanCustomizedFavorite.update_by like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("scanCustomizedFavorite.update_by not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.update_by in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("scanCustomizedFavorite.update_by not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.update_by between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("scanCustomizedFavorite.update_by not between", value1, value2, "updateBy");
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