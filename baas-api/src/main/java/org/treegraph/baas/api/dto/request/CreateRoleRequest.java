package org.treegraph.baas.api.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateRoleRequest {

  private String consId;

  private String name;

  private List<String> permissions;
}
