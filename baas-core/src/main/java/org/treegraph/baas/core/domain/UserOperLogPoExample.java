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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andOperTypeIsNull() {
            addCriterion("oper_type is null");
            return (Criteria) this;
        }

        public Criteria andOperTypeIsNotNull() {
            addCriterion("oper_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperTypeEqualTo(Integer value) {
            addCriterion("oper_type =", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotEqualTo(Integer value) {
            addCriterion("oper_type <>", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeGreaterThan(Integer value) {
            addCriterion("oper_type >", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("oper_type >=", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeLessThan(Integer value) {
            addCriterion("oper_type <", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeLessThanOrEqualTo(Integer value) {
            addCriterion("oper_type <=", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeIn(List<Integer> values) {
            addCriterion("oper_type in", values, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotIn(List<Integer> values) {
            addCriterion("oper_type not in", values, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeBetween(Integer value1, Integer value2) {
            addCriterion("oper_type between", value1, value2, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("oper_type not between", value1, value2, "operType");
            return (Criteria) this;
        }

        public Criteria andOperInfoIsNull() {
            addCriterion("oper_info is null");
            return (Criteria) this;
        }

        public Criteria andOperInfoIsNotNull() {
            addCriterion("oper_info is not null");
            return (Criteria) this;
        }

        public Criteria andOperInfoEqualTo(String value) {
            addCriterion("oper_info =", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoNotEqualTo(String value) {
            addCriterion("oper_info <>", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoGreaterThan(String value) {
            addCriterion("oper_info >", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoGreaterThanOrEqualTo(String value) {
            addCriterion("oper_info >=", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoLessThan(String value) {
            addCriterion("oper_info <", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoLessThanOrEqualTo(String value) {
            addCriterion("oper_info <=", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoLike(String value) {
            addCriterion("oper_info like", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoNotLike(String value) {
            addCriterion("oper_info not like", value, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoIn(List<String> values) {
            addCriterion("oper_info in", values, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoNotIn(List<String> values) {
            addCriterion("oper_info not in", values, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoBetween(String value1, String value2) {
            addCriterion("oper_info between", value1, value2, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperInfoNotBetween(String value1, String value2) {
            addCriterion("oper_info not between", value1, value2, "operInfo");
            return (Criteria) this;
        }

        public Criteria andOperTimeIsNull() {
            addCriterion("oper_time is null");
            return (Criteria) this;
        }

        public Criteria andOperTimeIsNotNull() {
            addCriterion("oper_time is not null");
            return (Criteria) this;
        }

        public Criteria andOperTimeEqualTo(LocalDateTime value) {
            addCriterion("oper_time =", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeNotEqualTo(LocalDateTime value) {
            addCriterion("oper_time <>", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeGreaterThan(LocalDateTime value) {
            addCriterion("oper_time >", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("oper_time >=", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeLessThan(LocalDateTime value) {
            addCriterion("oper_time <", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("oper_time <=", value, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeIn(List<LocalDateTime> values) {
            addCriterion("oper_time in", values, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeNotIn(List<LocalDateTime> values) {
            addCriterion("oper_time not in", values, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("oper_time between", value1, value2, "operTime");
            return (Criteria) this;
        }

        public Criteria andOperTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("oper_time not between", value1, value2, "operTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(LocalDateTime value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(LocalDateTime value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<LocalDateTime> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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