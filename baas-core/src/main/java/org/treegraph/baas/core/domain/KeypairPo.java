package org.treegraph.baas.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeypairPo implements Serializable {
    /**
     * 主键
     */
    private Long id;

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
     * 0-未删除，1-已删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改者
     */
    private String updateBy;

    private static final long serialVersionUID = 1L;
}