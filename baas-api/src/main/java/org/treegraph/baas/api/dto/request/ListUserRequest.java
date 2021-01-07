package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import org.treegraph.baas.api.dto.ListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListUserRequest extends ListRequest {

  private String account;

  private String name;

  private String email;

  private String phone;

  private Integer status;

  @Builder
  public ListUserRequest(int current, int pageSize, String account, String name, String email,
      String phone, Integer status) {
    super(current, pageSize);
    this.account = account;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.status = status;
  }
}
