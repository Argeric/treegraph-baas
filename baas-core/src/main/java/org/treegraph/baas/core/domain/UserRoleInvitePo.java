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
public class UserRoleInvitePo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 邀请id
     */
    private Long inviteId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 联盟id
     */
    private String consortiumId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 邀请状态，0-未授角色，1-已授角色
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}