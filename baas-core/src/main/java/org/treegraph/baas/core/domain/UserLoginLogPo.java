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
public class UserLoginLogPo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录的设备，取userAgent信息
     */
    private String device;

    /**
     * 1 h5 2 android  3ios 4 web
     */
    private Integer type;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录的时间
     */
    private Date loginTime;

    /**
     * 登陆的地址
     */
    private String ipAddress;

    private static final long serialVersionUID = 1L;
}