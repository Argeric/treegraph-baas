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
public class UserInvitePo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 邀请者
     */
    private String inviter;

    /**
     * 接受者
     */
    private String accepter;

    /**
     * 邀请token
     */
    private String token;

    /**
     * 过期时间，单位秒
     */
    private Integer expire;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 邀请时间
     */
    private LocalDateTime inviteTime;

    /**
     * 接受时间
     */
    private LocalDateTime acceptTime;

    private static final long serialVersionUID = 1L;
}