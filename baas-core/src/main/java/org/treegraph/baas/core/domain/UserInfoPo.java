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
public class UserInfoPo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 账号
     */
    private String account;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 加密后的登陆密码
     */
    private String password;

    /**
     * 尝试登陆次数
     */
    private Integer tryTimes;

    /**
     * 1-启用，2-禁用
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