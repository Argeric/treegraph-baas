package org.treegraph.baas.core.constant;

import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.CertificateSignType;

public class X509CertConstant {
	public static final String ALGO_ROOT = CryptoEnum.Algo.RSA.getCode();
	public static final String ALGO_CONS = CryptoEnum.Algo.RSA.getCode();
	public static final String ALGO_NODE = CryptoEnum.Algo.ECC.getCode();
	public static final String ALGO_RPC = CryptoEnum.Algo.RSA.getCode();

	public static final String SUBJECT_ROOT_CA = "CN=rootCA,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN";
	public static final String SUBJECT_CONS_CERT = "CN=baas.conflux-chain.org,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
	public static final String SUBJECT_RPC_CERT = "CN=baas.conflux-chain.org,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
	
	public static final String COMMON_NAME_CONSORTIUM_CERT= "baas.conflux-chain.org";
	
	public static final CertificateSignType DEFAULT_CERT_SIGN_TYPE = CryptoEnum.CertificateSignType.VAULT;
	public static final int EXPIRE_CONSORTIUM_CERT_IN_DYAS = 365;
	public static final String VAULT_ROLE_ISSUE_CERTIFICATE = "baasroot";
	public static final String VAULT_PKI_PATH = "pki_baas_" + ALGO_CONS;
}
