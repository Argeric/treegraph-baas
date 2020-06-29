package org.treegraph.baas.core.constant;

import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.SignCertificateRequest;

public class X509CertConstant {
	
	public static final String DEFAULT_ALGO = CryptoEnum.Algo.ECC.getCode();

	public static final String ROOT_CA_SUBJECT = "CN=rootCA,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN";

	public static final String CONSORTIUM_CERT_SUBJECT = "CN=consortiumCert,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
	
	public static final String CONSORTIUM_CERT_COMMON_NAME = "consortiumCert";
	
	public static final int CONSORTIUM_CERT_EXPIRE_DYAS = 365;
	
	public static final SignCertificateRequest SIGN_CERTIFICATE_REQUEST = CryptoEnum.SignCertificateRequest.VAULT;
	
	public static final String VAULT_ROLE_ISSUE_CERTIFICATE = "baasroot";
	
	public static final String VAULT_PKI_PATH = "pki_baas";
}
