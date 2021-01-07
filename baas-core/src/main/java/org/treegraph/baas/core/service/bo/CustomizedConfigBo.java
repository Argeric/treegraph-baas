package org.treegraph.baas.core.service.bo;

import java.util.List;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomizedConfigBo {

  private List<NodeCustomizedConfigPo> configList;

  private List<NodeCustomizedConfigPo> stepFactorConfigList;
}
