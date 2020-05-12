package org.treegraph.baas.core.entity.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.treegraph.baas.core.util.IdGenerator;

import lombok.Data;

/**
 * Created by haojx on 2018/3/27.
 */
@Data
public class SmsContentBo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String content;

	private List<String> mobile;
	
	private String serialNo;
	
	private String templateCode;

    private String triggerSource;

    private SmsContentBo() {
        this.triggerSource = "天机客服系统";
        this.serialNo = IdGenerator.generate();
    }

    public SmsContentBo(String mobile, String content, String templateCode) {
        this();
        this.content = content;
        this.mobile = new ArrayList<String>();
        this.mobile.add(mobile);
        this.templateCode = templateCode;
    }
}
