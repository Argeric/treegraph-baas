package org.treegraph.baas.core.enums;

import lombok.Getter;

public class CryptoEnum {
	
	 @Getter
	  public enum Algo {

	    RSA("rsa", "rsa算法"),

	    SM("sm", "国标加密算法"),

	    EC("ec", "椭圆曲线数字签名算法"),

	    ;

	    private String code;
	    
	    private String desc;

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

  @Getter
  public enum Pkcs {

    PKCS5("pkcs5", "pkcs5密钥加密算法"),

    PKCS8("pkcs8", "pkcs8密钥加密算法"),

    ;

    private String code;
    
    private String desc;

    Pkcs(String code, String desc) {
      this.code = code;
      this.desc = desc;
    }
    
    public static Pkcs getByCode(String code) {
        for (Pkcs s : Pkcs.values()) {
            if (s.code.equalsIgnoreCase(code)) {
                return s;
            }
        }
        return null;
    }
  }
}
