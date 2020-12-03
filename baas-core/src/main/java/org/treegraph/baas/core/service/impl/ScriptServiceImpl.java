package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.CharacterConstant.AT;
import static org.treegraph.baas.core.constant.CharacterConstant.COLON;
import static org.treegraph.baas.core.constant.CharacterConstant.COMMA;
import static org.treegraph.baas.core.constant.CharacterConstant.DOT;
import static org.treegraph.baas.core.constant.ConfluxConstant.DEFAULT_GRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_GRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_LOC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_CONTAINER_NETWORK;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_WEB_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_MONITOR_PORT_INTERVAL;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_RPC_PORT_INTERVAL;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_SWITCH;
import static org.treegraph.baas.core.constant.SplitterConstant.FILE_SEP;
import static org.treegraph.baas.core.constant.SplitterConstant.LINE_SEP;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.loadECKeyPairFromBase64;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.constant.MonitorConstant;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.service.ScriptService;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.FileUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.EncryptUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.treegraph.baas.core.util.crypto.x509.SignUtil;
import org.web3j.crypto.ECKeyPair;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class ScriptServiceImpl implements ScriptService {

  private final ConsortiumDas consortiumDas;

  private final NodeDas nodeDas;

  private final KeypairDas keypairDas;

  private final NodeCustomizedConfigDas nodeCustomizedConfigDas;

  private final BuildService buildService;

  private final KeypairBuildService keypairBuildService;

  @LogSkip
  public void downloadDeployScript(DownloadDeployScriptRequest req, HttpServletResponse resp)
      throws Exception {
    // check and build
    String consortiumId = req.getConsId();
    Assert.notNull(consortiumId, "consortiumId.not.found");
    List<NodePo> nodes = nodeDas.findAll(consortiumId, null);
    Assert.notEmpty(nodes, "node.not.found");
    Map<String, Map<String, String>> nodeScriptMap = buildDeployScript(consortiumId, nodes);

    // filter to download script
    Map<String, Map<String, String>> toDownloadNodeScriptMap = Maps.newHashMap();
    List<String> toDownloadNodeIdList = req.getNodeId();
    if (CollectionUtils.isNotEmpty(toDownloadNodeIdList)) {
      Set<String> toDownloadNodeIdSet = new HashSet<>(toDownloadNodeIdList);
      Set<String> toDownloadNodeKeySet = nodes.stream()
          .filter(item -> toDownloadNodeIdSet.contains(item.getId()))
          .map(item -> item.getNodeIp() + AT + item.getNodePort()).collect(Collectors.toSet());
      nodeScriptMap.keySet().forEach(item -> {
        if (toDownloadNodeKeySet.contains(item)) {
          toDownloadNodeScriptMap.put(item, nodeScriptMap.get(item));
        }
      });
    } else {
      toDownloadNodeScriptMap.putAll(nodeScriptMap);
    }

    // download deploy.zip file
    resp.setContentType("application/zip");
    resp.setHeader("Content-disposition", "attachment; filename=\"deploy.zip\"");
    ZipOutputStream zipOutputStream = new ZipOutputStream(resp.getOutputStream());
    for (Map.Entry<String, Map<String, String>> item : toDownloadNodeScriptMap.entrySet()) {
      String basePath = item.getKey();
      Map<String, String> scripts = item.getValue();
      for (String path : scripts.keySet()) {
        String content = scripts.get(path);
        zipOutputStream.putNextEntry(new ZipEntry(basePath + FILE_SEP + path));
        zipOutputStream.write(content.getBytes());
        zipOutputStream.closeEntry();
      }
    }
    zipOutputStream.close();
    log.debug("write deploy script finished[{}]", consortiumId);

    // update consortium establish step
    ConsortiumPo cons = buildService
        .buildToUpdateCons(consortiumId, EstablishStep.DOWNLOADED_SCRIPTS);
    consortiumDas.modifyCons(cons);
    log.debug("update consortium status finished[{}]", consortiumId);
  }

  private Map<String, Map<String, String>> buildDeployScript(String consId, List<NodePo> nodes)
      throws Exception {
    // certificate info
    keypairBuildService.geneRootKeypair();
    KeypairPo consKeypair = keypairBuildService.getConsortiumKeyPair(consId);
    PrivateKey privateKey = KeyUtil
        .privateKeyFromBase64(KeyType.CONSORTIUM.getAlgo().getCode(), consKeypair.getPrivateKey());

    // nodes info
    StringBuilder consortiumNodes = new StringBuilder();
    List<NodeKeypairBo> nodeKeypairBoList = new ArrayList<>();
    List<String> toDownloadNodeIpList = Lists.newArrayList();
    for (NodePo node : nodes) {
      NodeKeypairBo nodeKeypairBo = keypairBuildService.getNodeKeypairBo(consId, node);
      nodeKeypairBoList.add(nodeKeypairBo);
      ECKeyPair ecKeypair = loadECKeyPairFromBase64(nodeKeypairBo.getKeypair().getPublicKey(),
          nodeKeypairBo.getKeypair().getPrivateKey(), nodeKeypairBo.getKeypair().getAlgorithm());
      consortiumNodes.append("cfxnode://")
          .append(StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0'))
          .append(AT)
          .append(nodeKeypairBo.getNode().getNodeIp())
          .append(COLON)
          .append(nodeKeypairBo.getNode().getNodePort());
      if (!node.getId().equalsIgnoreCase(nodes.get(nodes.size() - 1).getId())) {
        consortiumNodes.append(COMMA);
      }
      nodes.forEach(item -> toDownloadNodeIpList.add(item.getNodeIp()));
    }

    // nodes info digest
    byte[] consNodesSHA3 = SignUtil.SHA256Digest(consortiumNodes.toString().getBytes());
    byte[] consNodesDigestCipher = EncryptUtil
        .codecByPrivateKey(KeyType.CONSORTIUM.getAlgo().getCode(), true, privateKey, consNodesSHA3);
    String consNodesDigest = Base64.toBase64String(consNodesDigestCipher);

    // node config
    List<NodeCustomizedConfigPo> configList = nodeCustomizedConfigDas.findAll(consId, null);
    Map<String, Map<String, String>> configMap = Maps.newHashMap();
    configList.stream()
        .filter(item -> !item.getNodeId().equals(IntTypeEnum.FALSE.getValue().toString()))
        .collect(Collectors.groupingBy(NodeCustomizedConfigPo::getNodeId))
        .forEach((nodeId, nodeConfigs) -> {
          Map<String, String> nodeConfigMap = nodeConfigs.stream().collect(Collectors
              .toMap(NodeCustomizedConfigPo::getPropertyName,
                  NodeCustomizedConfigPo::getPropertyValue));
          configMap.put(nodeId, nodeConfigMap);
        });

    // script content
    Map<String, Map<String, String>> nodeScriptContentMap = new HashMap<>();
    for (NodeKeypairBo nodeKeypair : nodeKeypairBoList) {
      KeypairPo keypair = nodeKeypair.getKeypair();
      ECKeyPair ecKeypair = loadECKeyPairFromBase64(keypair.getPublicKey(), keypair.getPrivateKey(),
          keypair.getAlgorithm());
      String ecPub = StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0');
      String ecPri = StringUtils.leftPad(ecKeypair.getPrivateKey().toString(16), 64, '0');
      NodePo node = nodeKeypair.getNode();
      GRpcKeypairCertBo gRpcKeypair = keypairBuildService
          .geneNewRpcKeypair(consId, null, toDownloadNodeIpList);
      StringBuffer scriptContent = new StringBuffer();
      append(scriptContent, "consortium_cert = \"\"\"" + consKeypair.getCertStr() + "\"\"\"");
      append(scriptContent, "consortium_nodes = \"\"\"" + consortiumNodes.toString() + "\"\"\"");
      append(scriptContent, "consortium_nodes_digest = \"" + consNodesDigest + "\"");
      append(scriptContent, "consortium_keypair = \"" + ecPub + COMMA + ecPri + "\"");
      append(scriptContent, "grpc_server_cert = \"\"\"" + gRpcKeypair.getCert() + "\"\"\"");
      append(scriptContent, "grpc_server_key = \"\"\"" + gRpcKeypair.getPrivateKey() + "\"\"\"");
      Map<String, String> configs = configMap.get(node.getId());
      StringBuffer customizedScriptContent = buildCustomizedDeployScript(ecKeypair, configs);
      Map<String, String> logMonitorScripts = buildLogMonitorScript(configs);
      Map<String, String> sysMonitorScripts = buildSysMonitorScript(configs);

      // build script map
      String startScriptContent = FileUtils.readFile(new ClassPathResource("scripts/start.sh"));
      Map<String, String> scripts = Maps.newHashMap();
      scripts.put("config.toml", scriptContent.toString());
      scripts.put("customized_config.toml", customizedScriptContent.toString());
      scripts.put("start.sh", startScriptContent);
      scripts.putAll(logMonitorScripts);
      scripts.putAll(sysMonitorScripts);
      nodeScriptContentMap.put(node.getNodeIp() + AT + node.getNodePort(), scripts);
    }

    // save key pair
    keypairDas.addKeyPairInfo(null, null, nodeKeypairBoList);
    log.debug("build deploy script finished[{}]", consId);
    return nodeScriptContentMap;
  }

  private StringBuffer buildCustomizedDeployScript(ECKeyPair ecKeypair,
      Map<String, String> configs) {
    // node customized script content
    StringBuffer customizedScriptContent = new StringBuffer();
    String jsonRpcHttpPort = configs.get(PROPERTY_JSONRPC_PORT);
    String jsonRpcLocalHttpPort = configs.get(PROPERTY_JSONRPC_LOC_PORT);
    customizedScriptContent.append(PROPERTY_JSONRPC_PORT + " = ").append(jsonRpcHttpPort)
        .append(LINE_SEP).append(LINE_SEP);
    customizedScriptContent.append(PROPERTY_JSONRPC_LOC_PORT + " = ").append(jsonRpcLocalHttpPort)
        .append(LINE_SEP).append(LINE_SEP);
    customizedScriptContent.append(PROPERTY_GRPC_PORT + "=").append(TEST_SWITCH
        ? (Integer.parseInt(jsonRpcLocalHttpPort) + TEST_RPC_PORT_INTERVAL) : DEFAULT_GRPC_PORT)
        .append(LINE_SEP).append(LINE_SEP);
    customizedScriptContent.append("mining_author = \"").append(ConfluxUtils.toAddress(ecKeypair))
        .append("\"")
        .append(LINE_SEP).append(LINE_SEP);
    customizedScriptContent.append("log_file = './conflux.log'")
        .append(LINE_SEP).append(LINE_SEP);
    customizedScriptContent.append("log_level = \"info\"")
        .append(LINE_SEP).append(LINE_SEP);
    return customizedScriptContent;
  }

  private Map<String, String> buildLogMonitorScript(Map<String, String> configs) {
    Map<String, String> scripts = Maps.newHashMap();
    List<String> files = Lists.newArrayList(
        "log_monitor/start_log_monitor.sh",
        "log_monitor/stop_log_monitor.sh",
        "log_monitor/docker-compose.yml",
        "log_monitor/.env"//from string with placeholder
    );
    files.forEach(item -> {
      String content;
      if (item.endsWith(".env")) {
        String webPort = configs.get(PROPERTY_LOG_MONITOR_WEB_PORT);
        String network = configs.get(PROPERTY_LOG_MONITOR_CONTAINER_NETWORK);
        content = MessageFormat.format(MonitorConstant.LOG_MONITOR_ENV_FILE, webPort,
            MonitorConstant.DEFAULT_HOST_LOG_PATH, webPort, network);
      } else {
        content = FileUtils.readFile(new ClassPathResource("scripts/" + item));

      }
      scripts.put(item, content);
    });
    return scripts;
  }

  private Map<String, String> buildSysMonitorScript(Map<String, String> configs) {
    Map<String, String> scripts = Maps.newHashMap();
    List<String> files = Lists.newArrayList(
        "sys_monitor/start_sys_monitor.sh",
        "sys_monitor/stop_sys_monitor.sh",
        "sys_monitor/docker-compose.yml",
        "sys_monitor/.env",//from string with placeholder
        "sys_monitor/grafana/dashboard/home.json",
        "sys_monitor/grafana/datasource/home.yaml",//from string with placeholder
        "sys_monitor/grafana/data/readme.md",
        "sys_monitor/prometheus/prometheus.yml"//from string with placeholder
    );
    files.forEach(item -> {
      String content;
      String logMonitorWebPort = configs.get(PROPERTY_LOG_MONITOR_WEB_PORT);
      String logMonitorNetwork = configs.get(PROPERTY_LOG_MONITOR_CONTAINER_NETWORK);
      String webPort = (Integer.parseInt(logMonitorWebPort) + TEST_MONITOR_PORT_INTERVAL) + "";
      String highestNetSeg = StringUtils.substringBefore(logMonitorNetwork, DOT);
      String leftNetSeg = StringUtils.substringAfter(logMonitorNetwork, DOT);
      String network = (Integer.parseInt(highestNetSeg) + 1) + DOT + leftNetSeg;
      if (item.endsWith(".env")) {
        content = MessageFormat
            .format(MonitorConstant.SYS_MONITOR_ENV_FILE, webPort, webPort, network);
      } else if (item.endsWith("home.yaml")) {
        content = MessageFormat
            .format(MonitorConstant.SYS_MONITOR_GRAFANA_DATASOURCE_FILE, webPort);
      } else if (item.endsWith("prometheus.yml")) {
        content = MessageFormat.format(MonitorConstant.SYS_MONITOR_PROMETHEUS_CONFIG_FILE, webPort);
      } else {
        content = FileUtils.readFile(new ClassPathResource("scripts/" + item));
      }
      scripts.put(item, content);
    });
    return scripts;
  }

  private void append(StringBuffer strBuf, String str) {
    strBuf.append(str).append(LINE_SEP).append(LINE_SEP);
  }
}
