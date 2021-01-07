package org.treegraph.baas.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodePo implements Serializable {

    private String id;


    private String consortiumId;


    private String blockchainId;


    private String nodeIp;


    private Integer nodePort;


    private String organizationName;


    private Integer status;


    private Integer membershipFinalStatus;


    private Integer membershipStatus;


    private String deployMsg;


    private Integer deleteFlag;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;


    private String createBy;


    private String updateBy;


    private static final long serialVersionUID = 1L;
}