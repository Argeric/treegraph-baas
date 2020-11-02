package org.treegraph.baas.core.service.impl;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.request.DownloadRpcCertRequest;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.service.CertService;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.util.LogSkip;

import javax.servlet.http.HttpServletResponse;
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
    
    private final VaultKeypairBuildService vaultKeypairBuildService;

	@LogSkip
	public void downloadRpcCert(DownloadRpcCertRequest req, HttpServletResponse resp) throws Exception {
		// check and build
		String consId = req.getConsId();
		Assert.notNull(consId, "consortiumId.not.found");

		// get certs
		KeypairPo consKeypair = vaultKeypairBuildService.getConsortiumKeyPair(consId, null);
		String serverCertFile = consKeypair.getCertStr();
		GRpcKeypairCertBo gRpcKeypair = vaultKeypairBuildService.geneNewRpcKeypair(consId, consKeypair, null);
		Map<String, String> certPathContentMap = Maps.newHashMap();
		certPathContentMap.put(SERVER_CERT_FILE, serverCertFile);
		certPathContentMap.put(RPC_CERT_CHAIN_FILE, gRpcKeypair.getCert());
		certPathContentMap.put(RPC_KEY_FILE, gRpcKeypair.getPrivateKey());

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
