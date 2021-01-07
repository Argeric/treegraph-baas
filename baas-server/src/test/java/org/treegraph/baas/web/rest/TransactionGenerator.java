package org.treegraph.baas.web.rest;

import java.math.BigInteger;
import java.util.*;

import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import conflux.web3j.jsonrpc.Cfx;
import conflux.web3j.jsonrpc.Request;
import conflux.web3j.request.Epoch;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import conflux.web3j.types.RawTransaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionGenerator extends TimerTask {
	
//    private List<NodeKeypairBo> nodeKeypairs;
//    private Map<String, String> jsonRpcHttpPortMap;
//    private Map<String, BigInteger> nonceMap = Maps.newHashMap();
//
//    public TransactionGenerator(List<NodeKeypairBo> nodeKeypairs, Map<String, String> jsonRpcHttpPortMap){
//    	this.nodeKeypairs = nodeKeypairs;
//    	this.jsonRpcHttpPortMap = jsonRpcHttpPortMap;
//    }

	BigInteger value = BigInteger.ZERO;
	//
    private BigInteger getNonce(String key){
//        BigInteger value = nonceMap.getOrDefault(key,BigInteger.ZERO);
//        nonceMap.put(key,value.add(BigInteger.ONE));
		value = value.add(BigInteger.ONE);
        return value;
    }
    @Override
    public void run() {
    	try {
	        //NodeKeypairBo sender = nodeKeypairs.get(new Random().nextInt(nodeKeypairs.size()));
	        String ip = "101.200.183.108";
	        String port = "12538";
	        String publicKey = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAENG/wRdIJ2O0qC+Slp7vmVDbJxv1RmQECQ6jztdSh8QzT6hCIWTPONf7XvgyRh9fjIq9wMfJLLKZ/egC8tQL2QA==";
	        String privateKey = "MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQgtJKJrnGLiSWhSr18tSJtlHRXWwswJ4L/MxSf/rujrvugBwYFK4EEAAqhRANCAAQ0b/BF0gnY7SoL5KWnu+ZUNsnG/VGZAQJDqPO11KHxDNPqEIhZM841/te+DJGH1+Mir3Ax8ksspn96ALy1AvZA";
	        String algo = "ECC";
	        String jsonRpcHttpPort = port;
	        String uri = "http://" + ip + ":" + port;
	        Cfx cfx = Cfx.create(uri);

	        
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(publicKey,
					privateKey, algo);
			String address = "0x" + ConfluxUtils.toAddress(ecKeypair);
			
	        BigInteger nonce= cfx.getNonce(address, Epoch.latestState()).sendAndGet();
	        BigInteger currentNonce= getNonce(address);
	        log.info("TransactionGenerator uri--->{}, address--->{}, nonce--->{}, currentNonce--->{}", uri, address, nonce, currentNonce);
	        
	        //if(currentNonce.compareTo(nonce) < 0){
	        	Request<BigInteger, BigIntResponse> epochRequest = cfx.getEpochNumber(Epoch.latestState());
	        	BigInteger epoch_height = epochRequest.sendAndGet();
	        	String receiveNodeId = "1000000000200000000030000000004000000000";
	        	RawTransaction tx = RawTransaction.create(currentNonce, new BigInteger("21000"), receiveNodeId, new BigInteger("1"), null, epoch_height, null);
	        	
	        	ECKeyPair ecKeyPair = KeyUtil.loadECKeyPairFromBase64(publicKey,
						privateKey, algo);
	        	String transaction = tx.sign(ecKeyPair);
	            Request<String, StringResponse> txRequest = cfx.sendRawTransaction(transaction);
	            String txResponse = txRequest.sendAndGet();
	            log.info("TransactionGenerator uri--->{}, address--->{}, txRequest--->{}, txResponse--->{}", uri, address, JSON.toJSONString(txRequest), txResponse);
	        //}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	public static void main(String[] args) {
//		Thread t = new Thread(new TransactionGenerator());
//		t.start();

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TransactionGenerator(), 100, 1000);

	}
}

