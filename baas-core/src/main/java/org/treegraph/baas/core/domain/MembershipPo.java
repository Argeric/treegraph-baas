package org.treegraph.baas.core.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 变更请求id
     */
    private String membershipChangeId;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 节点ip
     */
    private String nodeIp;

    /**
     * 节点端口
     */
    private Integer nodePort;

    private static final long serialVersionUID = 1L;
}