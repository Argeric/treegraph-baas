package org.treegraph.baas.core.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.request.DownloadRpcCertRequest;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.CertDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.CertificateRequestPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.CertService;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.util.LogSkip;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.treegraph.baas.core.constant.GRpcConstant.*;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class CertServiceImpl implements CertService {

	private final NodeDas nodeDas;

	private final CertDas certDas;
    
    private final VaultKeypairBuildService vaultKeypairBuildService;

    private final BuildService buildService;

	@LogSkip
	public void downloadRpcCert(DownloadRpcCertRequest req, HttpServletResponse resp) throws Exception {
		// check and build
		String consId = req.getConsId();
		Assert.notNull(consId, "consortiumId.not.found");
		List<NodePo> nodes = nodeDas.findAll(consId, null);
		Assert.notEmpty(nodes, "node.not.found");

		// get to support nodes
		List<String> toDownloadNodeIpList = Lists.newArrayList();
		List<String> toDownloadNodeIdList = req.getNodeId();
		if(CollectionUtils.isNotEmpty(toDownloadNodeIdList)) {
			Set<String>	toDownloadNodeIdSet = new HashSet<>(toDownloadNodeIdList);
			nodes.stream().filter(item -> toDownloadNodeIdSet.contains(item.getId()))
					.forEach(item -> toDownloadNodeIpList.add(item.getNodeIp()));
		} else{
			nodes.forEach(item -> toDownloadNodeIpList.add(item.getNodeIp()));
		}

		// get certs
		KeypairPo consKeypair = vaultKeypairBuildService.getConsortiumKeyPair(consId, null);
		String serverCertFile = consKeypair.getCertStr();
		GRpcKeypairCertBo gRpcKeypair = vaultKeypairBuildService.geneNewRpcKeypair(consId, consKeypair, null,
				toDownloadNodeIpList);
		Map<String, String> certPathContentMap = Maps.newHashMap();
		certPathContentMap.put(SERVER_CERT_FILE, serverCertFile);
		certPathContentMap.put(RPC_CERT_CHAIN_FILE, gRpcKeypair.getCert());
		certPathContentMap.put(RPC_KEY_FILE, gRpcKeypair.getPrivateKey());

		// record request
		CertificateRequestPo certificateRequest= buildService.buildCertificateRequest(nodes.get(0));
		certDas.add(certificateRequest);

		// download file
		resp.setContentType("application/zip");
		resp.setHeader("Content-disposition", "attachment; filename=\"rpc-cert.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream(resp.getOutputStream());
		Set<String> keySet = certPathContentMap.keySet();
		for (String key : keySet) {
			String value = certPathContentMap.get(key);
			zipOutputStream.putNextEntry(new ZipEntry(key));
			zipOutputStream.write(value.getBytes());
			zipOutputStream.closeEntry();
		}
		zipOutputStream.close();
		log.info("write rpc cert finished[{}]", consId);
	}
}
