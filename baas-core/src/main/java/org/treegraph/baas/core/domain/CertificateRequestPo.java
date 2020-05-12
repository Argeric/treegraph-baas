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
public class CertificateRequestPo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 区块链id
     */
    private String blockchainId;

    /**
     * 申请人id
     */
    private String applyUserId;

    /**
     * 证书url
     */
    private String certificateUrl;

    /**
     * 生成时间
     */
    private LocalDateTime generateTime;

    /**
     * 下载时间
     */
    private LocalDateTime downloadTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}