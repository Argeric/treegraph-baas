package org.treegraph.baas.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthInfo {

  private String consId;

  private List<String> roles;

  private List<String> permissions;
}
