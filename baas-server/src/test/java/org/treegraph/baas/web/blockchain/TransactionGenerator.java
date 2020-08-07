package org.treegraph.baas.web.blockchain;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.request.Epoch;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import conflux.web3j.types.RawTransaction;

class TransactionGenerator extends TimerTask {
	
    private List<NodeKeypairBo> nodeKeypairs;
    private Map<String, BigInteger> nonceMap;
    private Cfx cfx;
    
    public TransactionGenerator(List<NodeKeypairBo> nodeKeypairs){
    	this.nodeKeypairs = nodeKeypairs;
    }

    BigInteger getNonce(String key){
        BigInteger value = nonceMap.getOrDefault(key,BigInteger.ZERO);
        nonceMap.put(key,value.add(BigInteger.ONE));
        return value;
    }
    @Override
    public void run() {
        Request<BigInteger, BigIntResponse> epochRequest = cfx.getEpochNumber(Epoch.latestState());
        BigInteger epoch_height = epochRequest.sendAndGet();
        NodeKeypairBo sender = nodeKeypairs.get(new Random().nextInt(nodeKeypairs.size()));

        String nodeId = sender.getKeypair().getPublicKey();
        BigInteger nonce =cfx.getNonce(nodeId, Epoch.latestState());
        BigInteger currentNonce= getNonce(nodeId);
        if(currentNonce.compareTo(nonce) < 0){
        	RawTransaction tx = RawTransaction.create(currentNonce,21000,1,null,epoch_height,null);
        	ECKeyPair ecKeyPair = KeyUtil.loadECKeyPairFromBase64(sender.getKeypair().getPublicKey(),
        			sender.getKeypair().getPrivateKey(), sender.getKeypair().getAlgorithm());
            Request<String, StringResponse> txRequest = cfx.sendRawTransaction(tx.sign(ecKeyPair));
            txRequest.sendAndGet();
        }
    }
}
