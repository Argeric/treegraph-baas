package org.treegraph.baas.core.entity.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ClientBo {
    private Long id;
    private String idCardName;
    private String idCardNo;
    private String token;
    private Date tokenUpdateTime;
    private String authCode;
    private Date validDateEnd;
    /**
     *产品合同查询码
     */
    private String productCode;
}
