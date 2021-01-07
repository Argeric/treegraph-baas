package org.treegraph.baas.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import conflux.web3j.types.RawTransaction;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.ConsAdminInfo;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.*;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.service.impl.KeypairBuildService;
import org.treegraph.baas.core.service.impl.RpcSelectorService;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.FileUtils;
import org.treegraph.baas.core.util.Order;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class BlockchainCreationTests extends AbstractBaasIntegrationTests {

	private static String consId;

	private static String consAdminAccount;

	private static String blockchainId;

	private static List<String> nodeIds;

	@Autowired
	private BlockchainDas blockchainDas;

	@Autowired
	private UserInfoDas userInfoDas;

	@Autowired
	private RpcSelectorService rpcSelectorService;

	@Autowired
	private KeypairBuildService keypairBuildService;

	//=====================================================================================================================
	// test paras
	//=====================================================================================================================
	// create consortium
	private static final String name = "unittest_联盟名称";
	private static final String remark = "unittest_联盟描述";
	private static final String contact = "unittest_联盟联系人姓名";
	private static final String phone = "unittest_联盟联系人电话";
	private static final String email = "unittest_联盟联系人邮箱";
	// create consortium admin
	//public static final String userId = null;
	private static final String adminAccount = "unittest_联盟管理员账号";
	private static final String adminName = "unittest_联盟管理员姓名";
	private static final String adminEmail = "unittest_联盟管理员邮箱";
	private static final String adminPhone = "unittest_联盟管理员电话";
	private static final String adminPwd = "unittest_联盟管理员密钥";
	private static final String adminOrg = "unittest_联盟管理员组织";
	// create blockchain
	private static final String blockchainName = "unittest_区块链名称";
	private static final String blockchainAlertEmail = "unittest_区块链报警邮箱";
	// add node
	private static final String nodeIp = "101.200.183.108";
	private static final String nodeOrg = "unittest_节点组织";
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
	// construct
	//=====================================================================================================================
	@Test
	@Order(2)
	public void testCreateConsortium() throws Exception {
		log.info("[\nstep--->testCreateConsortium]=====================================================================================================================");
		String consName = name + System.currentTimeMillis();
		CreateConsRequest createConsRequest = CreateConsRequest.builder()
				.name(consName)
				.remark(remark)
				.contact(contact)
				.email(email)
				.phone(phone).build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createConsRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<String> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<String>>() {
				});
		consId = baasResult.getData();
		log.info("[step--->testCreateConsortium]consId--->{}", consId);
		Assert.assertNotNull("[step--->testCreateConsortium]", consId);
	}

	@Test
	@Order(3)
	public void testCreateConsortiumAdmin() throws Exception {
		log.info("[\nstep--->testCreateConsortiumAdmin]=====================================================================================================================");
		consAdminAccount = adminAccount + System.currentTimeMillis();
		ConsAdminInfo consAdminInfo = ConsAdminInfo.builder()
				.account(consAdminAccount)
				.name(adminName)
				.email(adminEmail)
				.phone(adminPhone)
				.pwd(adminPwd)
				.org(adminOrg).build();
		CreateConsAdminRequest createConsAdminRequest = CreateConsAdminRequest.builder()
				.consId(consId)
				.users(Lists.newArrayList(consAdminInfo)).build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/admin/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createConsAdminRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {
				});
		Boolean successFlag = baasResult.getData();
		log.info("[step--->testCreateConsortiumAdmin]successFlag--->{}", successFlag);
		Assert.assertTrue("[step--->testCreateConsortiumAdmin]", successFlag != null && successFlag);
	}

	@Test
	@Order(4)
	public void testCreateBlockchain() throws Exception {
		log.info("[\nstep--->testCreateBlockchain]=====================================================================================================================");
		CreateBlockchainRequest createBlockchainRequest = CreateBlockchainRequest.builder()
				.consId(consId)
				.name(blockchainName)
				.alertEmail(blockchainAlertEmail).build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/blockchain/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(createBlockchainRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<String> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<String>>() {
				});
		blockchainId = baasResult.getData();
		log.info("[step--->testCreateBlockchain]blockchainId--->{}", blockchainId);
		Assert.assertNotNull("[step--->testCreateBlockchain]", blockchainId);
	}

	@Test
	@Order(5)
	public void testAddNodesBeforeLaunchBlockchain() throws Exception {
		log.info("[\nstep--->testAddNodesBeforeLaunchBlockchain]=====================================================================================================================");
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
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/node/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(addNodesRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {
				});
		Boolean successFlag = baasResult.getData();
		log.info("[step--->testAddNodesBeforeLaunchBlockchain]successFlag--->{}", successFlag);
		Assert.assertTrue("[step--->testAddNodesBeforeLaunchBlockchain]", successFlag != null && successFlag);
	}

	@Test
	@Order(6)
	public void testDownloadScript() throws Exception {
		log.info("[\nstep--->testDownloadScript]=====================================================================================================================");
		DownloadDeployScriptRequest downloadDeployScriptRequest = DownloadDeployScriptRequest.builder()
				.consId(consId)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/script/download")
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
		log.info("[step--->testDownloadScript]len--->{}", content.length);
		Assert.assertTrue("[step--->testDownloadScript]", content.length > 0);
	}

	@Test
	@Order(7)
	public void testLaunchBlockchain() throws IOException, InterruptedException {
		log.info("[\nstep--->testLaunchBlockchain]=====================================================================================================================");
		File[] folders = new File(dirCreateBlockchain).listFiles();
		Assert.assertNotNull("[step--->testDownloadScript]folders is null", folders);
		for (File folder : folders){
			if(folder.isFile()) continue;
			ProcessBuilder builder = new ProcessBuilder(blockchainBinaryPath, "--customized-config",
					"customized_config.toml");
			builder.directory(folder);
			builder.redirectError(new File(folder + File.separator + "stderr"));
			builder.redirectOutput(new File(folder + File.separator + "stdout"));
			processes.add(builder.start());
			TimeUnit.SECONDS.sleep(5);
			log.info("[step--->testLaunchBlockchain]start node--->{}", folder.getName());
		}
		TimeUnit.SECONDS.sleep(60);
		log.info("[step--->testLaunchBlockchain]start blockchain completed!");
	}

	@Test
	@Order(8)
	public void testRechargeGenesis() throws Exception {
		log.info("[\nstep--->testRechargeGenesis]=====================================================================================================================");
		ListNodeRequest listNodeRequest = ListNodeRequest.builder()
				.consId(consId).build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/node/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listNodeRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListNodeResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<ListNodeResponse>>() {
				});
		nodeIds = baasResult.getData().getList().stream().map(NodeInfo::getId)
				.collect(Collectors.toList());
		log.info("[step--->testRechargeGenesis]list nodes--->{}", JSON.toJSONString(nodeIds));
		Assert.assertTrue("[step--->testRechargeGenesis]node not found", CollectionUtils.isNotEmpty(nodeIds));

		List<NodeKeypairBo> nodeKeypairs = nodeIds.stream().map(item -> keypairBuildService.getNodeKeypairBo(consId,
				NodePo.builder().consortiumId(consId).id(item).build())).collect(Collectors.toList());
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
			String transactionResult = rpcSelectorService.getRpcService().sendRawTransaction(node.getConsortiumId(),
					node.getId(), transactionSign);
			currentNonce = currentNonce.add(BigInteger.ONE);
			log.info("[step--->testRechargeGenesis]nonce--->{}, transactionSign->{},transactionResult--->{}", currentNonce, transactionSign, transactionResult);
			Assert.assertNotNull("[step--->testRechargeGenesis]null transactionResult", transactionResult);

//			BigInteger epochNum = rpcService.getBftEpochNumber(node.getConsortiumId(), node.getId());
//			currentNonce = currentNonce.add(BigInteger.ONE);
//			log.info("[step--->testRechargeGenesis]nonce--->{}, epochNum--->{}", currentNonce, epochNum);
		}
	}

	//=====================================================================================================================
	// deconstruct
	//=====================================================================================================================
	@Test
	@Order(9)
	public void testStopBlockchain() throws InterruptedException {
		TimeUnit.HOURS.sleep(24);
		log.info("[\nstep--->testStopBlockchain]=====================================================================================================================");
		for(Process process:processes) {
			if(process.isAlive()) {
				process.destroyForcibly();
			}
		}
	}

	@Test
	@Order(10)
	public void testRemoveScript() throws IOException {
		log.info("[\nstep--->testRemoveScript]=====================================================================================================================");
		FileUtil.cleanDir(dirCreateBlockchain);
	}

	@Test
	@Order(11)
	public void testRemoveNode() throws Exception {
		log.info("[\nstep--->testRemoveNode]=====================================================================================================================");
		for(String nodeId:nodeIds) {
			DeleteNodeRequest deleteNodeRequest = DeleteNodeRequest.builder()
					.id(nodeId).force(true).build();
			MvcResult mvcResult1 = getMockMvc().perform(MockMvcRequestBuilders.post("/node/del")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.header("X-Token", token)
					.content(JSON.toJSONString(deleteNodeRequest)))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
			BaasResult<Boolean> baasResult1 = JSON.parseObject(mvcResult1.getResponse().getContentAsString(),
					new TypeReference<BaasResult<Boolean>>() {
					});
			Boolean successFlag = baasResult1.getData();
			log.info("[step--->testRemoveNode]remove node--->{}", nodeId);
			Assert.assertTrue("[step--->testRemoveNode]remove node--->" + nodeId, successFlag != null && successFlag);
		}
		boolean successBlockchainFlag = removeBlockchain();
		log.info("[step--->testRemoveNode]successBlockchainFlag:{}, remove node completed!", successBlockchainFlag);
	}

	public boolean removeBlockchain() {
		log.info("[\nstep--->removeBlockchain]=====================================================================================================================");
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
		log.info("[\nstep--->testRemoveConsortiumAdmin]=====================================================================================================================");
		UserInfoPo admin = userInfoDas.findByAccount(consAdminAccount)
				.orElseThrow(() -> new RuntimeException("user.not.found"));
		String userId = admin.getId();
		DeleteUserRequest deleteUserRequest = DeleteUserRequest.builder()
				.id(userId).build();
		log.info("[\nstep--->testRemoveConsortiumAdmin]deleteUserRequest:{}", JSON.toJSONString(deleteUserRequest));
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/user/del")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(deleteUserRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {
				});
		Boolean successFlag = baasResult.getData();
		log.info("[step--->testRemoveConsortiumAdmin]remove admin--->{}", userId);
		Assert.assertTrue("[step--->testRemoveConsortiumAdmin]remove admin--->" + userId, successFlag != null && successFlag);
	}

	@Test
	@Order(13)
	public void testRemoveConsortium() throws Exception {
		log.info("[\nstep--->testRemoveConsortium]=====================================================================================================================");
		DeleteConsRequest deleteConsRequest = DeleteConsRequest.builder().id(consId).build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/cons/del")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(deleteConsRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<Boolean>>() {
				});
		Boolean successFlag = baasResult.getData();
		log.info("[step--->testRemoveConsortium]successFlag--->{}", successFlag);
		Assert.assertTrue("[step--->testRemoveConsortium]", successFlag != null && successFlag);
	}

	@Override
	boolean needLogin() {
		return true;
	}
}
