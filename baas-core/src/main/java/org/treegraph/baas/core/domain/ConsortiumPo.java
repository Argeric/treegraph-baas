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
public class ConsortiumPo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 联盟名称
     */
    private String name;

    /**
     * 联盟描述
     */
    private String description;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 联盟状态，0-创建中，1-运行中，2-已停止
     */
    private Integer status;

    /**
     * 0-未删除，1-已删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 联盟头像
     */
    private byte[] icon;

    private static final long serialVersionUID = 1L;
}