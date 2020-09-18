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
public class CertificateDeferPo implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String publicKey;

    /**
     * 
     */
    private String privateKey;

    /**
     * 
     */
    private String csrStr;

    /**
     * 
     */
    private String certStr;

    /**
     * 
     */
    private String algorithm;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 延期状态，21-发起延期请求，22-延期成功，23-延期失败
     */
    private Integer status;

    /**
     * 0-未删除，1-已删除
     */
    private Integer deleteFlag;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 
     */
    private String updateBy;

    private static final long serialVersionUID = 1L;
}