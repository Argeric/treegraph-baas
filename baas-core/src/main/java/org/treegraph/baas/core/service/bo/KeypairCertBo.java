package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeypairCertBo implements Serializable {

  /**
   * 加密算法，支持ecc，sm2， rsa
   */
  private String algorithm;

  /**
   * 公钥
   */
  private String publicKey;

  /**
   * 私钥
   */
  private String privateKey;

  /**
   * 证书签名请求字符串
   */
  private String csrStr;

  /**
   * 证书字符串
   */
  private String certStr;

  private static final long serialVersionUID = 1L;
}