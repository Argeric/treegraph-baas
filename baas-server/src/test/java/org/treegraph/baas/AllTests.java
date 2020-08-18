package org.treegraph.baas;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.DeleteNodeRequest;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.impl.VaultKeypairBuildService;
import org.treegraph.baas.core.util.FileUtils;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.types.RawTransaction;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TreeGraphBaasApp.class)
@Slf4j
public class AllTests {
	
	@Autowired
	private NodeDas nodeDas;
	
	@Autowired
	private VaultKeypairBuildService vaultKeypairBuildService;
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	private static String testConsortiumId = "940fcaad-0fa0-44bf-a59e-88dbaf202186";
	
	private static String testBlockchainId = "e225a92e-c863-4f32-bb17-d01175001e61";
	
	private static String binaryPath  = "/opt/software/tree-graph/target/release/conflux_production";
	
	private static String cfgRootPath = "/opt/service/testsuite";
	
	private static String scriptPath = cfgRootPath + "/zipfile";
	
	private static String configPath = cfgRootPath + "/unzipfile";
	
	private static String scriptName = "deploy.zip";
	
	private static String toAddNodeIp = "101.200.183.108";
	
	private static int toAddNodePort = 12345;
	
	
	private static MvcResult downloadDeployScriptMvcResult = null;
	
	private static List<NodeKeypairBo> nodeKeypairs = null;
	
	private static Map<String, String> jsonRpcHttpPortMap = Maps.newHashMap();
	
	private static String token = null;
	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		login();
		delAddedNode();
		generateScripts();
		getNodeKeypairs();
	}
	
	@Test
	public void testWriteDeployScript() throws Exception {
		/*
		 * write deploy script
		 */
		byte[] content = downloadDeployScriptMvcResult.getResponse().getContentAsByteArray();
		FileUtil.cleanDir(scriptPath);
		FileUtil.writeBytes(scriptPath+"/"+scriptName, content);
		log.info("write deploy script result--->completed!(len={})", content == null ? 0 : content.length);
		
    	/*
    	 * copy deploy file
    	 */
		if(!FileUtil.isExistingFolder(new File(configPath))) {
			FileUtil.mkdirs(configPath);
		}
		FileUtil.cleanDir(configPath);
		FileUtils.unZip(new File(scriptPath+"/"+scriptName), configPath);
		log.info("unzip deploy script result--->completed!");
		
		/*
		 * parse json rpc http port
		 */
		File path = new File(configPath);
		File[] subPaths = path.listFiles();
		Lists.newArrayList(subPaths).stream().forEach(subPath -> {
			File customizedConfigFile = Lists.newArrayList(subPath.listFiles()).stream().filter(configFile -> configFile.getName().equalsIgnoreCase("customized_config.toml")).findFirst().get();
			String jsonRpcHttpPort = parseJsonRpcHttpPort(customizedConfigFile);
			jsonRpcHttpPortMap.put(subPath.getName(), jsonRpcHttpPort);
		});
		log.info("parse json rpc http port result--->{}", JSON.toJSONString(jsonRpcHttpPortMap));
	}

    @Test
    public void testDeployBlockchain() throws IOException, InterruptedException {
        /*
         * deploy
         */
        List<Process> processes = new ArrayList<>(10);
        for (File folder : new File(configPath).listFiles()){
            ProcessBuilder builder = new ProcessBuilder(binaryPath, "--customized-config","customized_config.toml");
            builder.directory(folder);
            builder.redirectError(new File(folder+File.separator+"stderr"));
            builder.redirectOutput(new File(folder+File.separator+"stdout"));
            processes.add(builder.start());
            log.info("start conflux process--->{}", folder.getName());
        }
        log.info("start conflux process result--->completed!");
        TimeUnit.SECONDS.sleep(60);
    }
    
    @Test
    public void testRechargeGenesis() {
        /*
         * genesis private key :0x46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f
         * genesis total token 5 * 10 ** 36
         */
        BigInteger currentNonce = BigInteger.ZERO;
        for(NodeKeypairBo nodeKeypair : nodeKeypairs) {
        	KeypairPo keypair = nodeKeypair.getKeypair();
			RawTransaction tx = RawTransaction.create(currentNonce, new BigInteger("21000"), keypair.getPublicKey(),
					new BigInteger("100000000000000000000000000000000"), null, BigInteger.ZERO, null);
			ECKeyPair ecKeyPair = ECKeyPair
					.create(new BigInteger("46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f", 16));
			String txSign = tx.sign(ecKeyPair);
			
			NodePo node = nodeKeypair.getNode();
			String jsonRpcHttpPort = jsonRpcHttpPortMap.get(node.getNodeIp()+CharacterConstant.AT+node.getNodePort());
			String uri = "http://" + node.getNodeIp() + ":" + jsonRpcHttpPort;
			log.info("genesis recharge, nonce--->{}, uri--->{}", currentNonce, uri);
			
			Cfx cfx = Cfx.create(uri);
            String txResult = cfx.sendRawTransaction(txSign).sendAndGet();
            currentNonce = currentNonce.add(BigInteger.ONE);
            log.info("genesis recharge, nonce--->{}, result--->{}", currentNonce, txResult);
        }
    }
    
    @Test
    public void testMembershipChange() throws Exception{
        /*
         * start tx generator
         */
        Timer timer  = new Timer();
        timer.schedule(new TransactionGenerator(nodeKeypairs, jsonRpcHttpPortMap), 0 ,1000);
        TimeUnit.SECONDS.sleep(60 * 1);
        
        NodeInfo node1 = NodeInfo.builder()
        		.ip(toAddNodeIp).port(toAddNodePort+"").org("org1").build();
        AddNodesRequest addNodesRequest = AddNodesRequest.builder()
        		.consId(testConsortiumId)
        		.blockchainId(testBlockchainId)
        		.nodes(Lists.newArrayList(node1))
        		.build();
        MvcResult addResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/node/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(addNodesRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
        TimeUnit.SECONDS.sleep(60 * 3);
       	
        NodePo node11 = nodeDas.findByIpPort(node1.getIp(), Integer.parseInt(node1.getPort()));
        DeleteNodeRequest delNodeRequest = DeleteNodeRequest.builder()
        		.id(node11.getId()).ip(node1.getId()).port(node1.getPort()).build();
        MvcResult removeResult = mockMvc.perform(MockMvcRequestBuilders.post("/node/del")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(delNodeRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
        TimeUnit.SECONDS.sleep(60 * 60);
    }
    
    private void delAddedNode() {
    	nodeDas.deleteByIpPort(toAddNodeIp, toAddNodePort);
    }
    
	
	private void login() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/sys/login")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"account\":\"sys-admin\",\"password\":\"123456\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();

		BaasResult<LoginResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<LoginResponse>>() {});
		token = baasResult.getData().getToken();
		log.info("login token--->{}", token);
		Assert.assertNotNull("login error, no token return!", token);
	}
	
	private void generateScripts() throws Exception {
		// generate deploy script
		downloadDeployScriptMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/script/download")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content("{\"consId\":\""+testConsortiumId+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	private void getNodeKeypairs() {
        List<NodePo> nodes = nodeDas.findByConsIdAndChainId(testConsortiumId, testBlockchainId);
        nodeKeypairs = nodes.stream()
        		.map(item -> vaultKeypairBuildService.getNodeKeypair(testConsortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item, null))
				.collect(Collectors.toList());
	}
	
	private static String parseJsonRpcHttpPort(File customizedConfigFile) {
		try {
			String[] lines = FileUtil.readLines(customizedConfigFile);
			String[] keyValuePair = lines[0].split("=");
			return keyValuePair[1].trim();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
