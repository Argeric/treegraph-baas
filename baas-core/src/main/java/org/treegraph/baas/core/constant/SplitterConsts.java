package org.treegraph.baas.core.constant;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterConsts {
    public static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    public static final Splitter DOT_SPLITTER = Splitter.on(".").omitEmptyStrings().trimResults();
    public static final Joiner COMMA_JOINER = Joiner.on(",");
    
    
    public static void main(String[] args) {
    	System.out.println(COMMA_JOINER.join("consId", "roleId", "roleType"));
    	System.out.println(COMMA_SPLITTER.split("consId,roleId,roleType"));
	}
}
