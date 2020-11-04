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
     * 
     */
    private Long id;

    /**
     * 
     */
    private String consortiumId;

    /**
     * 
     */
    private String blockchainId;

    /**
     * 
     */
    private String applyUserId;

    /**
     * 
     */
    private Integer deleteFlag;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    private static final long serialVersionUID = 1L;
}