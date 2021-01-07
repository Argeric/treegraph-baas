package org.treegraph.baas.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeypairPo implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 公钥
   */
  private String publicKey;

  /**
   * 私钥
   */
  private String privateKey;

  /**
   * 证书签名请求字符串
   */
  private String csrStr;

  /**
   * 证书字符串
   */
  private String certStr;

  /**
   * 加密算法，支持ecc，sm2， rsa
   */
  private String algorithm;

  /**
   * 联盟id，根证书该字段为0
   */
  private String consortiumId;

  /**
   * 0-联盟密钥，1-节点密钥
   */
  private Integer keyType;

  /**
   * 0-未删除，1-已删除
   */
  private Integer deleteFlag;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 创建者
   */
  private String createBy;

  /**
   * 修改时间
   */
  private LocalDateTime updateTime;

  /**
   * 修改者
   */
  private String updateBy;

  private static final long serialVersionUID = 1L;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getCsrStr() {
    return csrStr;
  }

  public void setCsrStr(String csrStr) {
    this.csrStr = csrStr;
  }

  public String getCertStr() {
    return certStr;
  }

  public void setCertStr(String certStr) {
    this.certStr = certStr;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getConsortiumId() {
    return consortiumId;
  }

  public void setConsortiumId(String consortiumId) {
    this.consortiumId = consortiumId;
  }

  public Integer getKeyType() {
    return keyType;
  }

  public void setKeyType(Integer keyType) {
    this.keyType = keyType;
  }

  public Integer getDeleteFlag() {
    return deleteFlag;
  }

  public void setDeleteFlag(Integer deleteFlag) {
    this.deleteFlag = deleteFlag;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }
}