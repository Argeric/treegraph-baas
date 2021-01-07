package org.treegraph.baas.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanCustomizedFavoritePo implements Serializable {
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
    private Integer type;

    /**
     * 
     */
    private String hash;

    /**
     * 
     */
    private String remark;

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