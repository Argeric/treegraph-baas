package org.treegraph.baas;

import conflux.web3j.jsonrpc.Cfx;
import conflux.web3j.jsonrpc.Request;
import conflux.web3j.response.Status;

public class JSonRpcExample {

    public void getStatus(){
        String ip = "127.0.0.1";
        int rpcPort = 12537;
        String uri = "http://"+ip + ":"+ rpcPort;
        Cfx cfx = Cfx.create(uri);
        Request<Status, Status.Response> request = cfx.getStatus();
        Status response = request.sendAndGet();
    }
}
