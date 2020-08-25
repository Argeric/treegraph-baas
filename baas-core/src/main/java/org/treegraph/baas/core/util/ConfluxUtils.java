package org.treegraph.baas.core.util;
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public static String signMessage(byte[] message, ECKeyPair ecKeyPair) {
		Sign.SignatureData data = Sign.signMessage(message, ecKeyPair);
		byte[] rsv = new byte[data.getR().length + data.getS().length + data.getV().length];
		System.arraycopy(data.getR(), 0, rsv, 0, data.getR().length);
		System.arraycopy(data.getS(), 0, rsv, data.getR().length, data.getS().length);
		data.getV()[0] = (byte)(data.getV()[0] - 27);
		System.arraycopy(data.getV(), 0, rsv, data.getR().length + data.getS().length, data.getV().length);
		log.info(" message--->"+Numeric.toHexString(message)
		+ "\n r--->"+Numeric.toHexString(data.getR())
		+ "\n s--->"+Numeric.toHexString(data.getS())
		+ "\n v--->"+Numeric.toHexString(data.getV()));
		
		return Numeric.toHexString(rsv);
	}
}