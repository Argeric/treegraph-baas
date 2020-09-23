package org.treegraph.baas.core.enums;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class UserEnum {


    /**
     * 员工状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {

        /**
         * 冻结
         */
        FROZEN(0, "冻结"),

        /**
         * 启用
         */
        AVAILABLE(1, "启用"),
        ;

        private int code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }

        public static boolean isAvailable(int status) {
            return AVAILABLE.getCode() == status;
        }
        
        public static boolean isFrozen(int status) {
            return FROZEN.getCode() == status;
        }
    }
    
    /**
     * 角色
     */
    @Getter
    @AllArgsConstructor
    public enum Role {

        SYS_ADMIN(1, "系统管理员"),

        CONSORTIUM_ADMIN(2, "联盟管理员"),
        
        COMMON_USER(3, "普通角色"),
        ;

        private int code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code  == code) {
                    return s;
                }
            }
            return null;
        }

    }
    
	/**
	 * 权限
	 */
	@Getter
	@AllArgsConstructor
	public enum PermissionPoint {
		CREATE_CONS("createCons", "创建联盟"), 
		UPDATE_CONS("updateCons", "修改联盟"), 
		VIEW_CONS("viewCons", "查看联盟"),
		CREATE_CONS_ADMIN("createConsAdmin", "创建联盟管理员"), 
		CREATE_CONS_USER("createConsUser", "创建联盟用户"),
		UPDATE_CONS_USER("updateConsUser", "修改联盟用户"), 
		AUTH_CONS_USER("authConsUser", "给联盟用户分配权限"),
		VIEW_CONS_USER("viewConsUser", "查看联盟用户"),

		CREATE_BLOCKCHAIN("createBlockchain", "创建区块链"), 
		UPDATE_BLOCKCHAIN("updateBlockchain", "修改区块链"),
		VIEW_BLOCKCHAIN("viewBlockchain", "查看区块链信息"), 
		VIEW_BLOCKCHAIN_SCAN("viewBlockchainScan", "查看区块链浏览器"),
		GENERATE_CA("generateCA", "申请证书"),

		CREATE_NODE("createNode", "创建节点"),
		UPDATE_NODE("updateNode", "修改节点"), 
		VIEW_NODE_LOG("viewNodeLog", "查看节点日志"),
		VIEW_NODE_MONITOR("viewNodeMonitor", "查看节点运维信息"),
		;

		private String code;

		private String name;

		public static PermissionPoint getByCode(String code) {
			for (PermissionPoint s : PermissionPoint.values()) {
				if (s.code.equals(code)) {
					return s;
				}
			}
			return null;
		}
		
		public static List<PermissionPoint> getForConsAdmin(){
			List<PermissionPoint> permissionList = Lists.newArrayList(PermissionPoint.values());
			return permissionList.stream().filter(item -> !item.getCode().equals(PermissionPoint.CREATE_CONS.getCode()))
					.collect(Collectors.toList());
		}
	}
	
	/**
     * 邀请状态
     */
    @Getter
    @AllArgsConstructor
    public enum InviteStatus {

        INIT(0, "创建邀请记录"),

        SNED(1, "发送邀请"),
        
        ACCEPT(2, "接受邀请"),

        ;

        private int code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }
    }
    
    /**
     * 邀请角色状态
     */
    @Getter
    @AllArgsConstructor
    public enum InviteUserRoleStatus {

        NOT_ACCEPT(0, "未接受邀请"),

        ACCEPT(1, "已接受邀请"),
        
        ;

        private int code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }
    }

}
