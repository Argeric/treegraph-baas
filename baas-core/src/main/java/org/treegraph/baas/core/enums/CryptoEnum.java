package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CryptoEnum {

  @Getter
  public enum Algo {

    ECC("ECC", "ECC算法"),

    SM2("SM2", "SM2算法"),

    RSA("RSA", "RSA算法"),

    ;

    private final String code;

    private final String desc;

    Algo(String code, String desc) {
      this.code = code;
      this.desc = desc;
    }

    public static Algo getByCode(String code) {
      for (Algo s : Algo.values()) {
        if (s.code.equalsIgnoreCase(code)) {
          return s;
        }
      }
      return null;
    }
  }

  /**
   * 签发证书的类型
   */
  @Getter
  @AllArgsConstructor
  public enum CertSignType {

    BC(1, "bounce castle"),

    VAULT(2, "hashicorp vault"),
    ;

    private final Integer code;

    private final String desc;

    public static CertSignType getByCode(int code) {
      for (CertSignType s : CertSignType.values()) {
        if (s.code == code) {
          return s;
        }
      }
      return null;
    }
  }

  @Getter
  public enum Subject {

    ROOT_CA("CN=root.baas.conflux-chain.org,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN",
        "root.ca.subject"),

    CONSORTIUM("CN=consortium.baas.conflux-chain.org,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN",
        "consortium.ca.subject"),

    RPC("CN=rpc.baas.conflux-chain.org,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN", "rpc.cert.subject"),
    ;

    private final String code;

    private final String desc;

    Subject(String code, String desc) {
      this.code = code;
      this.desc = desc;
    }

    public static Subject getByCode(String code) {
      for (Subject s : Subject.values()) {
        if (s.code.equalsIgnoreCase(code)) {
          return s;
        }
      }
      return null;
    }
  }

  /**
   * 密钥类型
   */
  @Getter
  @AllArgsConstructor
  public enum KeyType {

    //strategy 1
    ROOT_CA(0, Algo.RSA, CertSignType.BC, Subject.ROOT_CA, "root.ca.keypair"),

    CONSORTIUM(1, Algo.RSA, CertSignType.BC, Subject.CONSORTIUM, "consortium.keypair"),

    NODE(2, Algo.ECC, CertSignType.BC, null, "node.keypair"),

    RPC(3, Algo.RSA, CertSignType.VAULT, Subject.RPC, "rpc.keypair"),

    //strategy 2
/*    ROOT_CA(0, Algo.RSA, CertSignType.VAULT, Subject.ROOT_CA, "root.ca.keypair"),

    CONSORTIUM(1, Algo.RSA, CertSignType.VAULT, Subject.CONSORTIUM, "consortium.keypair"),

    NODE(2, Algo.ECC, CertSignType.BC, null, "node.keypair"),

    RPC(3, Algo.RSA, CertSignType.VAULT, Subject.RPC, "rpc.keypair"),*/

    //strategy 3
/*    ROOT_CA(0, Algo.RSA, CertSignType.BC, Subject.ROOT_CA, "root.ca.keypair"),

    CONSORTIUM(1, Algo.RSA, CertSignType.BC, Subject.CONSORTIUM, "consortium.keypair"),

    NODE(2, Algo.ECC, CertSignType.BC, null, "node.keypair"),

    RPC(3, Algo.RSA, CertSignType.BC, Subject.RPC, "rpc.keypair"),*/

    ;

    private final Integer code;

    private final Algo algo;

    private final CertSignType signType;

    private final Subject subject;

    private final String desc;

    public static KeyType getByCode(int code) {
      for (KeyType s : KeyType.values()) {
        if (s.code == code) {
          return s;
        }
      }
      return null;
    }
  }
}
