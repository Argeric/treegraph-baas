package org.treegraph.baas.core.util;

import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;
import org.treegraph.baas.common.util.PermHelper;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
public class SysManager {

	public static boolean checkOwnCons(List<String> operConsIds) {
		if (PermHelper.getSysAdminFlag()) {
			return Boolean.TRUE;
		}
		Set<String> consIdSet = PermHelper.getConsIdSet();
		operConsIds.stream().forEach(item -> Assert.isTrue(consIdSet.contains(item), "无联盟操作权限[" + item + "]"));
		return Boolean.TRUE;
	}
}