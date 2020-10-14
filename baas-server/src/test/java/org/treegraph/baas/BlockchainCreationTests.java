package org.treegraph.baas;

import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
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
import org.treegraph.baas.api.dto.ConsAdminInfo;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DeleteNodeRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.LoginRequest;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.service.BlockchainRpcService;
import org.treegraph.baas.core.service.impl.VaultKeypairBuildService;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.FileUtils;
import org.treegraph.baas.core.util.Order;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import conflux.web3j.types.RawTransaction;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TreeGraphBaasApp.class)
@Slf4j
public class BlockchainCreationTests {
	
	private static boolean initialized = false; 
	
	private static MockMvc mockMvc;
	
	private static String token;
	
	private static String consId;
	
	private static String blockchainId;
	
	private static List<String> nodeIds;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private BlockchainDas blockchainDas;
	
	@Autowired
    private UserInfoDas userInfoDas;
	
	@Autowired
    private BlockchainRpcService rpcService;
	
	@Autowired
	private VaultKeypairBuildService vaultKeypairBuildService;
	
	//=====================================================================================================================
	// test paras
	//=====================================================================================================================
	// login
	private static final String account = "sys-admin";
	private static final String password = "123456";
	// create consortium
	private static final String name = "testunit_联盟名称";
	private static final String remark = "testunit_联盟描述";
	private static final String contact = "testunit_联盟联系人姓名";
	private static final String phone = "testunit_联盟联系人电话";
	private static final String email = "testunit_联盟联系人邮箱";
	// create consortium admin
	public static final String userId = null;
	private static final String adminAccount = "testunit_联盟管理员账号";
	private static final String adminName = "testunit_联盟管理员姓名";
	private static final String adminEmail = "testunit_联盟管理员邮箱";
	private static final String adminPhone = "testunit_联盟管理员电话";
	private static final String adminPwd = "testunit_联盟管理员密钥";
	private static final String adminOrg = "testunit_联盟管理员组织";
	// create blockchain
	private static final String blockchainName = "testunit_区块链名称";
	private static final String blockchainAlertEmail = "testunit_区块链报警邮箱";
	// add node
	private static final String nodeIp = "101.200.183.108";
	private static final String nodeOrg = "testunit_节点组织";
	private static final String nodePort_1 = "32323";
	private static final String nodePort_2 = "32324";
	private static final String nodePort_3 = "32325";
	private static final String nodePort_4 = "32326";
	// download script
	private static final String dirCreateBlockchain = "/opt/service/testsuite/blockchain-creation";
	// deploy blockchain
	private static final List<Process> processes = Lists.newArrayList();
	private static final String blockchainBinaryPath  = "/opt/software/tree-graph/target/release/conflux_production";
	// recharge genesis
	private static final String genesisPrivateKey = "46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f";
	private static final String gasFee = "21000";
	private static final String rechargeTokenPerNode = "100000000000000000000000";//genesis total token 5 * 10 ** 36
	
	//=====================================================================================================================
	// prepare
	//=====================================================================================================================
	@Before
	public void setup() {
		if(!initialized) {
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		}
	}
	
	//=====================================================================================================================
	// constract
	//=====================================================================================================================
	@Test
	@Order(1)
	public void testLogin() throws Exception {
		log.info("[step--->testLogin]=====================================================================================================================");
		LoginRequest loginRequest = LoginRequest.builder()
				.account(account)
				.password(password).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/sys/login")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSON.toJSONString(loginRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<LoginResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<LoginResponse>>() {});
		token = baasResult.getData().getToken();
		log.info("[step--->testLogin]token--->{}", token);
		Assert.assertNotNull("[step--->testLogin]", token);
	}
	
	@Test
	@Order(2)
	public void testCreateConsortium() throws Exception {
		log.info("[step--->testCreateConsortium]=====================================================================================================================");
		CreateConsRequest createConsRequest = CreateConsRequest.builder()
				.name(name)
				.remark(remark)
				.contact(contact)
				.email(email)
				.phone(phone).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createConsRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<String> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<String>>() {});
		consId = baasResult.getData();
		log.info("[step--->testCreateConsortium]consId--->{}", consId);
		Assert.assertNotNull("[step--->testCreateConsortium]", consId);
	}
	
	@Test
	@Order(3)
	public void testCreateConsortiumAdmin() throws Exception {
		log.info("[step--->testCreateConsortiumAdmin]=====================================================================================================================");
		ConsAdminInfo consAdminInfo = ConsAdminInfo.builder()
				.account(adminAccount)
				.name(adminName)
				.email(adminEmail)
				.phone(adminPhone)
				.pwd(adminPwd)
				.org(adminOrg).build();
		CreateConsAdminRequest createConsAdminRequest = CreateConsAdminRequest.builder()
				.consId(consId)
				.users(Lists.newArrayList(consAdminInfo)).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/admin/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createConsAdminRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {});
		Boolean succFlag = baasResult.getData();
		log.info("[step--->testCreateConsortiumAdmin]succFlag--->{}", succFlag);
		Assert.assertTrue("[step--->testCreateConsortiumAdmin]", succFlag != null && succFlag);
	}
	
	@Test
	@Order(4)
	public void testCreateBlockchain() throws Exception {
		log.info("[step--->testCreateBlockchain]=====================================================================================================================");
		CreateBlockchainRequest createBlockchainRequest = CreateBlockchainRequest.builder()
				.consId(consId)
				.name(blockchainName)
				.alertEmail(blockchainAlertEmail).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/blockchain/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createBlockchainRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<String> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<String>>() {});
		blockchainId = baasResult.getData();
		log.info("[step--->testCreateBlockchain]blockchainId--->{}", blockchainId);
		Assert.assertNotNull("[step--->testCreateBlockchain]", blockchainId);
	}
	
	@Test
	@Order(5)
	public void testAddNodesBeforeLanchBlockchain() throws Exception {
		log.info("[step--->testAddNodesBeforeLanchBlockchain]=====================================================================================================================");
		NodeInfo node1 = NodeInfo.builder()
				.ip(nodeIp).port(nodePort_1).org(nodeOrg).build();
		NodeInfo node2 = NodeInfo.builder()
				.ip(nodeIp).port(nodePort_2).org(nodeOrg).build();
		NodeInfo node3 = NodeInfo.builder()
				.ip(nodeIp).port(nodePort_3).org(nodeOrg).build();
		NodeInfo node4 = NodeInfo.builder()
				.ip(nodeIp).port(nodePort_4).org(nodeOrg).build();
		AddNodesRequest addNodesRequest = AddNodesRequest.builder()
				.consId(consId)
				.blockchainId(blockchainId)
				.nodes(Lists.newArrayList(node1, node2, node3, node4))
				.build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/node/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(addNodesRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {});
		Boolean succFlag = baasResult.getData();
		log.info("[step--->testAddNodesBeforeLanchBlockchain]succFlag--->{}", succFlag);
		Assert.assertTrue("[step--->testAddNodesBeforeLanchBlockchain]", succFlag != null && succFlag);
	}
	
	@Test
	@Order(6)
	public void testDownloadScript() throws Exception {
		log.info("[step--->testDownloadScript]=====================================================================================================================");
		DownloadDeployScriptRequest downloadDeployScriptRequest = DownloadDeployScriptRequest.builder()
				.consId(consId)
				.build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/script/download")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(downloadDeployScriptRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		byte[] content = mvcResult.getResponse().getContentAsByteArray();
		if(!FileUtil.isExistingFolder(new File(dirCreateBlockchain))) {
			FileUtil.mkdirs(dirCreateBlockchain);
		}
		FileUtil.cleanDir(dirCreateBlockchain);
		FileUtil.writeBytes(dirCreateBlockchain + "/deploy.zip", content);
		FileUtils.unZip(new File(dirCreateBlockchain + "/deploy.zip"),
				dirCreateBlockchain);
		log.info("[step--->testDownloadScript]len--->{}", content == null ? 0 : content.length);
		Assert.assertTrue("[step--->testDownloadScript]", content != null && content.length > 0);
	}
	
	@Test
	@Order(7)
	public void testLanchBlockchain() throws IOException, InterruptedException {
		log.info("[step--->testLanchBlockchain]=====================================================================================================================");
        File[] folders = new File(dirCreateBlockchain).listFiles();
        for (File folder : folders){
        	if(folder.isFile()) continue;
			ProcessBuilder builder = new ProcessBuilder(blockchainBinaryPath, "--customized-config",
					"customized_config.toml");
			builder.directory(folder);
			builder.redirectError(new File(folder + File.separator + "stderr"));
			builder.redirectOutput(new File(folder + File.separator + "stdout"));
			processes.add(builder.start());
			TimeUnit.SECONDS.sleep(5);
			log.info("[step--->testLanchBlockchain]start node--->{}", folder.getName());
        }
        TimeUnit.SECONDS.sleep(10);
        log.info("[step--->testLanchBlockchain]start blockchain completed!");
	}
	
	@Test
	@Order(8)
	public void testRechargeGenesis() throws Exception {
		log.info("[step--->testRechargeGenesis]=====================================================================================================================");
		ListNodeRequest listNodeRequest = ListNodeRequest.builder()
				.consId(consId).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/node/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listNodeRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListNodeResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<ListNodeResponse>>() {});
		nodeIds = baasResult.getData().getList().stream().map(item -> item.getId())
				.collect(Collectors.toList());
		log.info("[step--->testRechargeGenesis]list nodes--->{}", JSON.toJSONString(nodeIds));
		Assert.assertTrue("[step--->testRechargeGenesis]node not found", CollectionUtils.isNotEmpty(nodeIds));
		
		List<NodeKeypairBo> nodeKeypairs = nodeIds.stream().map(item -> vaultKeypairBuildService.getNodeKeypair(consId,
				ALGO_NODE, NodePo.builder().consortiumId(consId).id(item).build(), null)).collect(Collectors.toList());
		log.info("[step--->testRechargeGenesis]node keypairs--->{}", JSON.toJSONString(nodeKeypairs));
		Assert.assertTrue("[step--->testRechargeGenesis]node keypair not found", CollectionUtils.isNotEmpty(nodeKeypairs));
		
        BigInteger currentNonce = BigInteger.ZERO;
        for(NodeKeypairBo nodeKeypair : nodeKeypairs) {
			KeypairPo keypair = nodeKeypair.getKeypair();
			String address = "0x" + ConfluxUtils.toAddress(KeyUtil.loadECKeyPairFromBase64(keypair.getPublicKey(),
					keypair.getPrivateKey(), keypair.getAlgorithm()));
			RawTransaction transaction = RawTransaction.create(currentNonce, new BigInteger(gasFee), address,
					new BigInteger(rechargeTokenPerNode), null, BigInteger.ZERO, null);
			ECKeyPair genesisKeyPair = ECKeyPair
					.create(new BigInteger(genesisPrivateKey, 16));
			String transactionSign = transaction.sign(genesisKeyPair);
			
			NodePo node = nodeKeypair.getNode();
			String transactionResult = rpcService.sendRawTransaction(node.getConsortiumId(), node.getId(),
					transactionSign);
			currentNonce = currentNonce.add(BigInteger.ONE);
			log.info("[step--->testRechargeGenesis]nonce--->{}, transactionResult--->{}", currentNonce, transactionResult);
			Assert.assertTrue("[step--->testRechargeGenesis]null transactionResult", transactionResult != null);
        }
    }
	
	//=====================================================================================================================
	// deconstract
	//=====================================================================================================================
	@Test
	@Order(9)
	public void testStopBlockchain() {
		log.info("[step--->testStopBlockchain]=====================================================================================================================");
		for(Process process:processes) {
			if(process.isAlive()) {
				process.destroyForcibly();
			}
		}
	}
	
	@Test
	@Order(10)
	public void testRemoveScript() throws IOException {
		log.info("[step--->testRemoveScript]=====================================================================================================================");
		FileUtil.cleanDir(dirCreateBlockchain);
	}
	
	@Test
	@Order(11)
	public void testRemoveNode() throws Exception {
		log.info("[step--->testRemoveNode]=====================================================================================================================");
		for(String nodeId:nodeIds) {
			DeleteNodeRequest deleteNodeRequest = DeleteNodeRequest.builder()
					.id(nodeId).force(true).build();
			MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/node/del")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.header("X-Token", token)
					.content(JSON.toJSONString(deleteNodeRequest)))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
			BaasResult<Boolean> baasResult1 = JSON.parseObject(mvcResult1.getResponse().getContentAsString(),
					new TypeReference<BaasResult<Boolean>>() {});
			Boolean succFlag = baasResult1.getData();
			log.info("[step--->testRemoveNode]remove node--->{}", nodeId);
			Assert.assertTrue("[step--->testRemoveNode]remove node--->" + nodeId, succFlag != null && succFlag);
		}
		boolean succBlockchainFlag = removeBlockchain();
		log.info("[step--->testRemoveNode]succBlockchainFlag:{}, remove node completed!", succBlockchainFlag);
	}
	
	public boolean removeBlockchain() {
		log.info("[step--->removeBlockchain]=====================================================================================================================");
		BlockChainPo blockChainPo = BlockChainPo.builder()
				.id(blockchainId)
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		return blockchainDas.modify(blockChainPo);
	}
	
	@Test
	@Order(12)
	public void testRemoveConsortiumAdmin() throws Exception {
		log.info("[step--->testRemoveConsortiumAdmin]=====================================================================================================================");
		UserInfoPo admin = userInfoDas.findByAccount(adminAccount)
				.orElseThrow(() -> new RuntimeException("user.not.found"));
		String userId = admin.getId();
		DeleteUserRequest deleteUserRequest = DeleteUserRequest.builder()
				.id(userId).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/del")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(deleteUserRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {});
		Boolean succFlag = baasResult.getData();
		log.info("[step--->testRemoveConsortiumAdmin]remove admin--->{}", userId);
		Assert.assertTrue("[step--->testRemoveConsortiumAdmin]remove admin--->" + userId, succFlag != null && succFlag);
	}
	
	@Test
	@Order(13)
	public void testRemoveConsortium() throws Exception {
		log.info("[step--->testRemoveConsortium]=====================================================================================================================");
		DeleteConsRequest deleteConsRequest = DeleteConsRequest.builder().id(consId).build();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cons/del")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(deleteConsRequest)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {});
		Boolean succFlag = baasResult.getData();
		log.info("[step--->testRemoveConsortium]succFlag--->{}", succFlag);
		Assert.assertTrue("[step--->testRemoveConsortium]", succFlag != null && succFlag);
	}
}
