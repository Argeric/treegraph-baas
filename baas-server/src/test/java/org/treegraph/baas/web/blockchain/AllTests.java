package org.treegraph.baas.web.blockchain;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.treegraph.baas.TreeGraphBaasApp;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.response.LoginResponse;
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
	
	private static String rootPath = "/opt/service/testsuite";
	
	private static String scriptPath = rootPath + "/zipfile";
	
	private static String configPath = rootPath + "/unzipfile";
	
	private static String scriptName = "deploy.zip";
	
	private static String testConsortiumId = "940fcaad-0fa0-44bf-a59e-88dbaf202186";
	
	private static String testBlockchainId = "e225a92e-c863-4f32-bb17-d01175001e61";
	
	private static String token = null;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testLogin() throws Exception {
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
	
	@Test
	public void testDownloadDeployScript() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/script/download")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content("{\"consId\":\""+testConsortiumId+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		
		byte[] content = mvcResult.getResponse().getContentAsByteArray();
		log.info("download deploy script content.len--->{}", content == null ? 0 : content.length);
		FileUtil.cleanDir(scriptPath);
		FileUtil.writeBytes(scriptPath+"/"+scriptName, content);
		log.info("download deploy script result--->downloaded");
	}

    @Test
    public void testDeployBlockchain() throws IOException, InterruptedException {
    	/*
    	 * copy deploy file
    	 */
        String binaryPath  = "/opt/software/tree-graph/target/release/conflux_production";
		if(!FileUtil.isExistingFolder(new File(configPath))) {
			FileUtil.mkdirs(configPath);
		}
		FileUtil.cleanDir(configPath);
		FileUtils.unZip(new File(scriptPath+"/"+scriptName), configPath);
		log.info("unzip deploy script result--->unziped");

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
        log.info("start conflux process completed!");
        TimeUnit.SECONDS.sleep(60);

        // get node keypair
        List<NodePo> nodes = nodeDas.findByConsIdAndChainId(testConsortiumId, testBlockchainId);
        List<NodeKeypairBo> nodeKeypairs = nodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(testConsortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item))
				.collect(Collectors.toList());
        
        /*
         * genesis private key :0x46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f
         * genesis total token 5 * 10 ** 36
         */
        int jsonRpcPort = 12537;
        BigInteger currentNonce = BigInteger.ZERO;
        for(NodeKeypairBo nodeKeypair: nodeKeypairs) {
        	KeypairPo keypair = nodeKeypair.getKeypair();
			RawTransaction tx = RawTransaction.create(currentNonce, new BigInteger("21000"), keypair.getPublicKey(),
					new BigInteger("100000000000000000000000000000000"), null, BigInteger.ZERO, null);
			ECKeyPair ecKeyPair = ECKeyPair
					.create(new BigInteger("46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f", 16));
			String txSign = tx.sign(ecKeyPair);
			
			NodePo node = nodeKeypair.getNode();
			String uri = "http://" + node.getNodeIp() + ":" + jsonRpcPort;
			log.info("genesis recharge uri--->{}", uri);
			Cfx cfx = Cfx.create(uri);
            String txResult = cfx.sendRawTransaction(txSign).sendAndGet();
            currentNonce = currentNonce.add(BigInteger.ONE);
            log.info("genesis recharge result--->{}", txResult);
            jsonRpcPort ++ ;
        }

        /*
         * start tx generator
         */
        Timer timer  = new Timer();
        timer.schedule(new TransactionGenerator(nodeKeypairs), 0 ,1000);
        TimeUnit.SECONDS.sleep(60 * 60);
//        for
//        	add_validator
//        	sleep(30)
//        	check_validator added
//        	
//        	randome remove
//        	slepp
//        	check_validato
    }
}
