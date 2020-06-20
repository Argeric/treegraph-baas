package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CryptoEnum {

	@Getter
	public enum Algo {

		ECC("ECC", "ECC算法"),

		SM2("SM2", "SM2算法"),

		RSA("RSA", "RSA算法"),

		;

		private String code;

		private String desc;

		Algo(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static Algo getByCode(String code) {
			for (Algo s : Algo.values()) {
				if (s.code.equalsIgnoreCase(code)) {
					return s;
				}
			}
			return null;
		}
	}

	@Getter
	public enum Pkcs {

		PKCS5("pkcs5", "pkcs5密钥加密算法"),

		PKCS8("pkcs8", "pkcs8密钥加密算法"),

		;

		private String code;

		private String desc;

		Pkcs(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static Pkcs getByCode(String code) {
			for (Pkcs s : Pkcs.values()) {
				if (s.code.equalsIgnoreCase(code)) {
					return s;
				}
			}
			return null;
		}
	}

	/**
	 * 密钥类型
	 */
	@Getter
	@AllArgsConstructor
	public enum KeyType {

		CONSORTIUM(1, "联盟类型"),

		NODE(2, "节点类型"),;

		private Integer code;

		private String desc;

		public static KeyType getByCode(int code) {
			for (KeyType s : KeyType.values()) {
				if (s.code == code) {
					return s;
				}
			}
			return null;
		}
	}
}
