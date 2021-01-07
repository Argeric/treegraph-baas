package org.treegraph.baas.core.service.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgBo implements Serializable {

  private String consortiumId;

  private String organizationName;

  private static final long serialVersionUID = 1L;
}