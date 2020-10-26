package org.treegraph.baas.core.constant;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterConstant {
    public static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    public static final Joiner COMMA_JOINER = Joiner.on(",");
    public static final String LINE_SEP = System.getProperty("line.separator");
    public static final String FILE_SEP = System.getProperty("file.separator");
}
