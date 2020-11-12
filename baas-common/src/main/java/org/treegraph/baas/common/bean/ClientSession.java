package org.treegraph.baas.common.bean;

import java.io.Serializable;

public class ClientSession implements Serializable {

  private static final long serialVersionUID = -7014239157443767275L;

  private Long clientId;

  private String idCardName;

  private String idCardNo;

  public ClientSession() {
  }

  public ClientSession(Long clientId, String idCardName, String idCardNo) {
    this.clientId = clientId;
    this.idCardName = idCardName;
    this.idCardNo = idCardNo;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public String getIdCardName() {
    return idCardName;
  }

  public void setIdCardName(String idCardName) {
    this.idCardName = idCardName;
  }

  public String getIdCardNo() {
    return idCardNo;
  }

  public void setIdCardNo(String idCardNo) {
    this.idCardNo = idCardNo;
  }
}

