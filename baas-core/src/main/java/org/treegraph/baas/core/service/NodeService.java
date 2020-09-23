package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.DeleteNodeRequest;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.ModifyNodeRequest;
import org.treegraph.baas.api.dto.request.QueryNodeDetailRequest;
import org.treegraph.baas.api.dto.request.QueryNodeLogRequest;
import org.treegraph.baas.api.dto.response.ListNodeResponse;

public interface NodeService {

	ListNodeResponse listNodes(ListNodeRequest req);

	NodeInfo getNodeDetail(QueryNodeDetailRequest req);

	String getNodeLog(QueryNodeLogRequest req);

	boolean modifyNode(ModifyNodeRequest req);
	
	boolean deleteNode(DeleteNodeRequest req) throws Exception;
}
