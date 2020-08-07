package org.treegraph.baas.core.epoch.change;

import javax.annotation.Resource;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.request.Epoch;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import conflux.web3j.types.RawTransaction;
import org.apache.tomcat.jni.Proc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.treegraph.baas.core.service.EmailService;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;

class TransactionGenerator extends TimerTask {
    List<Node> nodes;
    Map<String, BigInteger> nonceMap;
    Cfx cfx;

    BigInteger getNonce(String key){
        BigInteger value = nonceMap.getOrDefault(key,BigInteger.ZERO);
        nonceMap.put(key,value.add(BigInteger.ONE));
        return value;
    }
    @Override
    public void run() {
        Request<BigInteger, BigIntResponse> epochRequest = cfx.getEpochNumber(Epoch.latestState());
        BigInteger epoch_height = epochRequest.sendAndGet();
        Node sender = nodes.get(new Random().nextInt(nodes.len()));

        RawTransaction tx = RawTransaction.create(getNonce(node.id),21000,1,null,epoch_height,null);
        Request<String, StringResponse> txRequest = cfx.sendRawTransaction(tx.sign(...);
        txRequest.sendAndGet();
    }
}
