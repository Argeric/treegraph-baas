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
public class MembershipChangePo implements Serializable {
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
     * 0-删除节点，1-添加节点
     */
    private Integer changeType;

    /**
     * 变更状态
     */
    private Integer status;

    /**
     * 参与校验的节点
     */
    private String validatorNodes;

    /**
     * 变更前bftEpochNumber
     */
    private Long preBftEpochNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    private static final long serialVersionUID = 1L;
}