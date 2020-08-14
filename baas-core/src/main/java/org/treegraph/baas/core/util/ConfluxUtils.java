package org.treegraph.baas.core.util;
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

public class ConfluxUtils {
	
	public static String toAddress(ECKeyPair ecKeypair) {
		String address = Keys.getAddress(ecKeypair);
		return "1" + address.substring(1);
	}
	
	public static String toAddress(String publicKey, String privateKey) {
		ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(publicKey,
				privateKey, CryptoEnum.Algo.ECC.getCode());
		return toAddress(ecKeypair);
	}
	
	public static String toJsonRpcUri(String nodeIp, int jsonRpcHttpPort) {
		return "http://"+nodeIp + CharacterConstant.COLON + jsonRpcHttpPort;
	}
	
	public static String toIpAtPort(String nodeIp, int jsonRpcHttpPort) {
		return nodeIp + CharacterConstant.AT + jsonRpcHttpPort;
	}
}