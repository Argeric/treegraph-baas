package org.treegraph.baas.core.domain;

import java.io.Serializable;
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
    private String userId;

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
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

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