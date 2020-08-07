package org.treegraph.baas.web.blockchain;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
import org.treegraph.baas.TreeGraphBaasApp;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.NodePo;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import conflux.web3j.Cfx;
import conflux.web3j.types.RawTransaction;
import jodd.io.FileUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TreeGraphBaasApp.class)
public class BlockchainTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
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
		Assert.assertNotNull("login error, no token return!", token);
	}
	
	@Test
	public void testDownloadDeployScript() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/script/download")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content("{\"consortiumId\":\"d4f142bb-8098-486e-8508-212dafc369b7\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}

    @Test
    public void testEpochChange() throws IOException, InterruptedException {
    	/*
    	 * copy deploy file
    	 */
        String binaryPath  = "/opt/software/tree-graph/target/release/conflux-production";
        String configPath = "/opt/service/testsuite";
		if(!FileUtil.isExistingFolder(new File(configPath))) {
			FileUtil.mkdirs(configPath);
		}
		FileUtil.cleanDir(configPath);
		FileUtil.copyFile(new File("./deploy.zip"), new File(configPath + "/deploy.zip"));

        /*
         * deploy
         */
        List<Process> processes = new ArrayList<>(10);
        for (File folder : new File(configPath).listFiles()){
            ProcessBuilder builder = new ProcessBuilder(binaryPath);
            builder.directory(folder);
            builder.redirectError(new File(folder+File.separator+"stderr"));
            builder.redirectOutput(new File(folder+File.separator+"stdout"));
            processes.add(builder.start());
        }
        Thread.sleep(20);

        /*
         * genesis private key :0x46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f
         * genesis total token 5 * 10 ** 36
         */
        List<NodeKeypairBo> nodeKeypairs = Lists.newArrayList();
        for(NodeKeypairBo nodeKeypair: nodeKeypairs) {
        	NodePo node = nodeKeypair.getNode();
            Cfx cfx = Cfx.create("http://" + node.getNodeIp() + ":" + node.getNodePort());
            RawTransaction tx = RawTransaction.create();
			ECKeyPair ecKeyPair = ECKeyPair
					.create(new BigInteger("46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f", 16));
            cfx.sendRawTransaction(tx.sign(ecKeyPair));
        }

        /*
         * start tx generator
         */
        Timer timer  = new Timer();
        timer.schedule(new TransactionGenerator(nodeKeypairs), 0 ,1000);
    }
    
    public static void main(String[] args) throws IOException {
    	String srcFile = "/Users/wangzhenting/Documents/testsuite.txt";
    	String configPath = "/Users/wangzhenting/Documents/testsuite";
		if(!FileUtil.isExistingFolder(new File(configPath))) {
			FileUtil.mkdirs(configPath);
		}
		FileUtil.cleanDir(configPath);
		FileUtil.copyFile(new File(srcFile), new File(configPath + "/deploy.zip"));
	}
}
