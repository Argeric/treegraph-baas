package org.treegraph.baas.core.constant;

public class X509CertConstant {

  public static final String VAULT_PKI_PATH_ROOT_CA ="baas-root";
  public static final String VAULT_ROLE_CONS_CA_ISSUER = "cons_ca_issuer";
  public static final String VAULT_ROLE_RPC_CERT_ISSUER = "rpc_cert_issuer";
  public static final int TTL_ROOT_CA_IN_DAY = 365 * 10;
  public static final int TTL_CONSORTIUM_CA_IN_DAY = 365 * 5;
  public static final int TTL_RPC_CERT_IN_DAY = 365;
}
