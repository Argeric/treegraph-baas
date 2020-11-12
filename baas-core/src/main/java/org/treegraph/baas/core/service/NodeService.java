package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.*;
import org.treegraph.baas.api.dto.response.ListNodeBriefResponse;
import org.treegraph.baas.api.dto.response.ListNodeResponse;

public interface NodeService {

  ListNodeResponse listNodes(ListNodeRequest req);

  ListNodeBriefResponse listNodeBriefs(ListNodeBriefRequest req);

  NodeInfo getNodeDetail(QueryNodeDetailRequest req);

  String getNodeLog(QueryNodeLogRequest req);

  boolean modifyNode(ModifyNodeRequest req);

  boolean deleteNode(DeleteNodeRequest req) throws Exception;
}
