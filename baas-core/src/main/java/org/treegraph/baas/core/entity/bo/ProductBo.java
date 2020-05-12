package org.treegraph.baas.core.entity.bo;

import java.util.Date;

import lombok.Data;

@Data
public class ProductBo {
    private Long id;

    private Integer productId;

    private String productCode;

    private String productName;

    private String channel;

    private String appname;

    private String smsTemplate;
    
    private String alpayBackUrl;

    private String signature;

    private Date createTime;

    private Date updateTime;

    private String remarks;
}