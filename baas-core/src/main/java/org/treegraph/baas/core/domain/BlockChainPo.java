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
public class BlockChainPo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 区块链名称
     */
    private String blockchainName;

    /**
     * 报警邮箱
     */
    private String alarmEmail;

    /**
     * 共识策略，1-PBFT,2-RAFT,3-POW,4-POS
     */
    private Integer consensusAlgorithm;

    /**
     * 底层加密算法，1-数图加密算法
     */
    private Integer encryptAlgorithm;

    /**
     * 运行状态，0-未启动，1-运行中，2-已停止
     */
    private Integer status;

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