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

        public Criteria andCertificateUrlIsNull() {
            addCriterion("certificateRequest.certificate_url is null");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlIsNotNull() {
            addCriterion("certificateRequest.certificate_url is not null");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlEqualTo(String value) {
            addCriterion("certificateRequest.certificate_url =", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlNotEqualTo(String value) {
            addCriterion("certificateRequest.certificate_url <>", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlGreaterThan(String value) {
            addCriterion("certificateRequest.certificate_url >", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlGreaterThanOrEqualTo(String value) {
            addCriterion("certificateRequest.certificate_url >=", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlLessThan(String value) {
            addCriterion("certificateRequest.certificate_url <", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlLessThanOrEqualTo(String value) {
            addCriterion("certificateRequest.certificate_url <=", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlLike(String value) {
            addCriterion("certificateRequest.certificate_url like", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlNotLike(String value) {
            addCriterion("certificateRequest.certificate_url not like", value, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlIn(List<String> values) {
            addCriterion("certificateRequest.certificate_url in", values, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlNotIn(List<String> values) {
            addCriterion("certificateRequest.certificate_url not in", values, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlBetween(String value1, String value2) {
            addCriterion("certificateRequest.certificate_url between", value1, value2, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andCertificateUrlNotBetween(String value1, String value2) {
            addCriterion("certificateRequest.certificate_url not between", value1, value2, "certificateUrl");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeIsNull() {
            addCriterion("certificateRequest.generate_time is null");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeIsNotNull() {
            addCriterion("certificateRequest.generate_time is not null");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time =", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time <>", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeGreaterThan(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time >", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time >=", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeLessThan(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time <", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.generate_time <=", value, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeIn(List<LocalDateTime> values) {
            addCriterion("certificateRequest.generate_time in", values, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("certificateRequest.generate_time not in", values, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("certificateRequest.generate_time between", value1, value2, "generateTime");
            return (Criteria) this;
        }

        public Criteria andGenerateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("certificateRequest.generate_time not between", value1, value2, "generateTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeIsNull() {
            addCriterion("certificateRequest.download_time is null");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeIsNotNull() {
            addCriterion("certificateRequest.download_time is not null");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.download_time =", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeNotEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.download_time <>", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeGreaterThan(LocalDateTime value) {
            addCriterion("certificateRequest.download_time >", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.download_time >=", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeLessThan(LocalDateTime value) {
            addCriterion("certificateRequest.download_time <", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("certificateRequest.download_time <=", value, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeIn(List<LocalDateTime> values) {
            addCriterion("certificateRequest.download_time in", values, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeNotIn(List<LocalDateTime> values) {
            addCriterion("certificateRequest.download_time not in", values, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("certificateRequest.download_time between", value1, value2, "downloadTime");
            return (Criteria) this;
        }

        public Criteria andDownloadTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("certificateRequest.download_time not between", value1, value2, "downloadTime");
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