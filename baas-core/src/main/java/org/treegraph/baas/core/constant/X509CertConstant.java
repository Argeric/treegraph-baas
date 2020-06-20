package org.treegraph.baas.core.constant;

import org.treegraph.baas.core.enums.CryptoEnum;

public class X509CertConstant {
	
	public static final String DEFAULT_ALGO = CryptoEnum.Algo.ECC.getCode();

	public static final String ROOT_CA_SUBJECT = "CN=rootCA,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN";

	public static final String CONSORTIUM_CERT_SUBJECT = "CN=consortiumCert,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
}
