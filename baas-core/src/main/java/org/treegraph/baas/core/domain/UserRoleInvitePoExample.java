package org.treegraph.baas.core.domain;

import java.util.ArrayList;
import java.util.List;

public class UserRoleInvitePoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserRoleInvitePoExample() {
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
            addCriterion("userRoleInvite.id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("userRoleInvite.id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("userRoleInvite.id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("userRoleInvite.id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("userRoleInvite.id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("userRoleInvite.id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("userRoleInvite.id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("userRoleInvite.id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("userRoleInvite.id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("userRoleInvite.id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("userRoleInvite.id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("userRoleInvite.id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andInviteIdIsNull() {
            addCriterion("userRoleInvite.invite_id is null");
            return (Criteria) this;
        }

        public Criteria andInviteIdIsNotNull() {
            addCriterion("userRoleInvite.invite_id is not null");
            return (Criteria) this;
        }

        public Criteria andInviteIdEqualTo(Long value) {
            addCriterion("userRoleInvite.invite_id =", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotEqualTo(Long value) {
            addCriterion("userRoleInvite.invite_id <>", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdGreaterThan(Long value) {
            addCriterion("userRoleInvite.invite_id >", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdGreaterThanOrEqualTo(Long value) {
            addCriterion("userRoleInvite.invite_id >=", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdLessThan(Long value) {
            addCriterion("userRoleInvite.invite_id <", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdLessThanOrEqualTo(Long value) {
            addCriterion("userRoleInvite.invite_id <=", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdIn(List<Long> values) {
            addCriterion("userRoleInvite.invite_id in", values, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotIn(List<Long> values) {
            addCriterion("userRoleInvite.invite_id not in", values, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdBetween(Long value1, Long value2) {
            addCriterion("userRoleInvite.invite_id between", value1, value2, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotBetween(Long value1, Long value2) {
            addCriterion("userRoleInvite.invite_id not between", value1, value2, "inviteId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("userRoleInvite.user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("userRoleInvite.user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("userRoleInvite.user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("userRoleInvite.user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("userRoleInvite.user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("userRoleInvite.user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("userRoleInvite.user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("userRoleInvite.user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("userRoleInvite.user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("userRoleInvite.user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("userRoleInvite.user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("userRoleInvite.user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("userRoleInvite.role_id is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("userRoleInvite.role_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(String value) {
            addCriterion("userRoleInvite.role_id =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(String value) {
            addCriterion("userRoleInvite.role_id <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(String value) {
            addCriterion("userRoleInvite.role_id >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.role_id >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(String value) {
            addCriterion("userRoleInvite.role_id <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.role_id <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLike(String value) {
            addCriterion("userRoleInvite.role_id like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotLike(String value) {
            addCriterion("userRoleInvite.role_id not like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<String> values) {
            addCriterion("userRoleInvite.role_id in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<String> values) {
            addCriterion("userRoleInvite.role_id not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(String value1, String value2) {
            addCriterion("userRoleInvite.role_id between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(String value1, String value2) {
            addCriterion("userRoleInvite.role_id not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIsNull() {
            addCriterion("userRoleInvite.consortium_id is null");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIsNotNull() {
            addCriterion("userRoleInvite.consortium_id is not null");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdEqualTo(String value) {
            addCriterion("userRoleInvite.consortium_id =", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotEqualTo(String value) {
            addCriterion("userRoleInvite.consortium_id <>", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdGreaterThan(String value) {
            addCriterion("userRoleInvite.consortium_id >", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdGreaterThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.consortium_id >=", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLessThan(String value) {
            addCriterion("userRoleInvite.consortium_id <", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLessThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.consortium_id <=", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdLike(String value) {
            addCriterion("userRoleInvite.consortium_id like", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotLike(String value) {
            addCriterion("userRoleInvite.consortium_id not like", value, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdIn(List<String> values) {
            addCriterion("userRoleInvite.consortium_id in", values, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotIn(List<String> values) {
            addCriterion("userRoleInvite.consortium_id not in", values, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdBetween(String value1, String value2) {
            addCriterion("userRoleInvite.consortium_id between", value1, value2, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andConsortiumIdNotBetween(String value1, String value2) {
            addCriterion("userRoleInvite.consortium_id not between", value1, value2, "consortiumId");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameIsNull() {
            addCriterion("userRoleInvite.organization_name is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameIsNotNull() {
            addCriterion("userRoleInvite.organization_name is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameEqualTo(String value) {
            addCriterion("userRoleInvite.organization_name =", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameNotEqualTo(String value) {
            addCriterion("userRoleInvite.organization_name <>", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameGreaterThan(String value) {
            addCriterion("userRoleInvite.organization_name >", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameGreaterThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.organization_name >=", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameLessThan(String value) {
            addCriterion("userRoleInvite.organization_name <", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameLessThanOrEqualTo(String value) {
            addCriterion("userRoleInvite.organization_name <=", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameLike(String value) {
            addCriterion("userRoleInvite.organization_name like", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameNotLike(String value) {
            addCriterion("userRoleInvite.organization_name not like", value, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameIn(List<String> values) {
            addCriterion("userRoleInvite.organization_name in", values, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameNotIn(List<String> values) {
            addCriterion("userRoleInvite.organization_name not in", values, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameBetween(String value1, String value2) {
            addCriterion("userRoleInvite.organization_name between", value1, value2, "organizationName");
            return (Criteria) this;
        }

        public Criteria andOrganizationNameNotBetween(String value1, String value2) {
            addCriterion("userRoleInvite.organization_name not between", value1, value2, "organizationName");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("userRoleInvite.`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("userRoleInvite.`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("userRoleInvite.`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("userRoleInvite.`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("userRoleInvite.`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("userRoleInvite.`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("userRoleInvite.`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("userRoleInvite.`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("userRoleInvite.`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("userRoleInvite.`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("userRoleInvite.`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("userRoleInvite.`status` not between", value1, value2, "status");
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