package org.treegraph.baas.core.constant;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

public class SplitterConsts {
    public static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    public static final Splitter DOT_SPLITTER = Splitter.on(".").omitEmptyStrings().trimResults();
    
    public static void main(String[] args) {
    	System.out.println(StringUtils.substringAfterLast("HelloController.creatUser", "."));
	}
}
