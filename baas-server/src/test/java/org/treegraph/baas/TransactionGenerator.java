package org.treegraph.baas;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.request.Epoch;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import conflux.web3j.types.RawTransaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionGenerator extends TimerTask {
	
    private List<NodeKeypairBo> nodeKeypairs;
    private Map<String, String> jsonRpcHttpPortMap;
    private Map<String, BigInteger> nonceMap = Maps.newHashMap();
    
    public TransactionGenerator(List<NodeKeypairBo> nodeKeypairs, Map<String, String> jsonRpcHttpPortMap){
    	this.nodeKeypairs = nodeKeypairs;
    	this.jsonRpcHttpPortMap = jsonRpcHttpPortMap;
    }

    private BigInteger getNonce(String key){
        BigInteger value = nonceMap.getOrDefault(key,BigInteger.ZERO);
        nonceMap.put(key,value.add(BigInteger.ONE));
        return value;
    }
    @Override
    public void run() {
    	try {
	        NodeKeypairBo sender = nodeKeypairs.get(new Random().nextInt(nodeKeypairs.size()));
	        String jsonRpcHttpPort = jsonRpcHttpPortMap.get(sender.getNode().getNodeIp()+CharacterConstant.AT+sender.getNode().getNodePort());
	        String uri = "http://" + sender.getNode().getNodeIp() + ":" + jsonRpcHttpPort;
	        log.info("TransactionGenerator uri--->{}", uri);
	        Cfx cfx = Cfx.create(uri);
	        
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(sender.getKeypair().getPublicKey(),
					sender.getKeypair().getPrivateKey(), sender.getKeypair().getAlgorithm());
			String address = "0x1" + Keys.getAddress(ecKeypair).substring(1);
			
			log.info("TransactionGenerator address--->{}", address);
	        BigInteger nonce= cfx.getNonce(address, Epoch.latestState()).sendAndGet();
	        log.info("TransactionGenerator nonce--->{}", nonce);
	        BigInteger currentNonce= getNonce(address);
	        
	        if(currentNonce.compareTo(nonce) < 0){
	        	Request<BigInteger, BigIntResponse> epochRequest = cfx.getEpochNumber(Epoch.latestState());
	        	BigInteger epoch_height = epochRequest.sendAndGet();
	        	String receiveNodeId = "1000000000200000000030000000004000000000";
	        	RawTransaction tx = RawTransaction.create(currentNonce, new BigInteger("21000"), receiveNodeId, new BigInteger("1"), null, epoch_height, null);
	        	log.info("TransactionGenerator tx--->{}", JSON.toJSONString(tx));
	        	
	        	ECKeyPair ecKeyPair = KeyUtil.loadECKeyPairFromBase64(sender.getKeypair().getPublicKey(),
							sender.getKeypair().getPrivateKey(), sender.getKeypair().getAlgorithm());
	        	log.info("TransactionGenerator sender--->{}", JSON.toJSONString(sender));
	        	String transaction = tx.sign(ecKeyPair);
	        	log.info("TransactionGenerator transaction--->{}", JSON.toJSONString(transaction));
	            Request<String, StringResponse> txRequest = cfx.sendRawTransaction(transaction);
	            log.info("TransactionGenerator txRequest--->{}", txRequest);
	            String result = txRequest.sendAndGet();
	            log.info("TransactionGenerator result--->{}", result);
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
