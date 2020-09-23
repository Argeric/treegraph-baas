package org.treegraph.baas.core.das.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeKeypairInfoBo implements Serializable {
    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;
    
    /**
     * 证书
     */
    private String certStr;

    /**
     * 加密算法，支持ecc，sm2， rsa
     */
    private String algorithm;

    /**
     * 联盟id，根证书该字段为0
     */
    private String consortiumId;

    /**
     * 0-联盟密钥，1-节点密钥
     */
    private Integer keyType;

    /**
     * 节点ip
     */
    private String nodeIp;

    /**
     * 节点端口
     */
    private Integer nodePort;

    private static final long serialVersionUID = 1L;
}