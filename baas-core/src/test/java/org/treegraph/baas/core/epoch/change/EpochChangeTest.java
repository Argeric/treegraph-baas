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
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.EmailService;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpochChangeTest {
	
	private ConsortiumService consortiumService;

    @Test
    public void testEpochChange() throws IOException, InterruptedException {

        //TODO get configuration files

        String binaryPath  = "/opt/software/tree-graph/target/release/conflux-production";
        String configPath = "xxx";

        //deploy
        List<Process> processes = new ArrayList<>(10);
        for (File folder : new File(configPath).listFiles()){
            ProcessBuilder builder = new ProcessBuilder(binaryPath);
            builder.directory(folder);
            builder.redirectError(new File(folder+File.separator+"stderr"));
            builder.redirectOutput(new File(folder+File.separator+"stdout"));
            processes.add(builder.start());
        }

        Thread.sleep(20);
        //some port
        //some ip

        //genesis private key :0x46b9e861b63d3509c88b7817275a30d22d62c8cd8fa6486ddee35ef0d8e0495f
        //genesis total token 5 * 10 ** 9 * 10 ** 18 * 10 ** 6
        for(node: nodes) {
            Cfx cfx = Cfx.create("http://" + node.ip + ":" + node.port);
            RawTransaction tx = RawTransaction.create();
            cfx.sendRawTransaction(tx.sign(node.key_pair));
        }

        Timer timer  = new Timer();
        timer.schedule(new TransactionGenerator(nodes), 0 ,1000);
    }
}
