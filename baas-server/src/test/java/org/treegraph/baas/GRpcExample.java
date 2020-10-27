package org.treegraph.baas;

import conflux.web3j.grpc.GrpcWeb3j;
import conflux.web3j.response.Status;

import javax.net.ssl.SSLException;

public class GRpcExample {

    public void getStatus() throws SSLException {
        String ip = "127.0.0.1";
        int rpcPort = 12537;
        String serverCertFile = "./grpc/server.crt";
        String rpcCertFile = "./grpc/rpc.crt";
        String rpcKey = "./grpc/key.pem";
        GrpcWeb3j cfx = new GrpcWeb3j(ip, rpcPort, serverCertFile, rpcCertFile, rpcKey);
        cfx.getStatus()
    }
}
