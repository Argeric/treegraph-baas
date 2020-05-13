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
public class UserOperLogPo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 操作类型
     */
    private Integer operType;

    /**
     * 操作code
     */
    private String operCode;

    /**
     * 操作信息
     */
    private String operInfo;

    /**
     * 操作时间
     */
    private LocalDateTime operTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}