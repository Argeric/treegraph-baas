package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import java.security.KeyPair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeypairBo implements Serializable {

  /**
   * 密钥对
   */
  private KeyPair keypair;

  /**
   * 公钥
   */
  private String publicKey;

  /**
   * 私钥
   */
  private String privateKey;

  private static final long serialVersionUID = 1L;
}