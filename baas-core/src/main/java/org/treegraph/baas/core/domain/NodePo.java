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
public class NodePo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 区块链id
     */
    private String blockchainId;

    /**
     * 节点ip
     */
    private String nodeIp;

    /**
     * 节点端口
     */
    private Integer nodePort;

    /**
     * 机构名称
     */
    private String organizationName;

    /**
     * 0-未启动，1-运行中，2-异常
     */
    private Integer status;

    /**
     * 21-添加进行中，22-添加成功，23-添加失败，31-删除进行中，32-删除成功，33-删除失败
     */
    private Integer membershipStatus;

    /**
     * 0-未删除，1-已删除
     */
    private Integer deleteFlag;

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