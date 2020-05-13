package org.treegraph.baas.core.das;

public interface UserOperLogDas {
	
	void addOperLog(String userId, String userName, String ip, String operCode, String operInfo);
}
