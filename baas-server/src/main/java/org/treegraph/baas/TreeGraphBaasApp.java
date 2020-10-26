package org.treegraph.baas;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.treegraph.baas.core.util.ApplicationStartup;

import javax.crypto.Cipher;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.Security;
import java.util.Map;

//import org.springframework.context.annotation.ImportResource;

@SuppressWarnings("unchecked")
@SpringBootApplication
@PropertySource(value = { "classpath:config-biz.properties", "classpath:config-yw.properties" })
@EnableConfigurationProperties
@EnableScheduling
@EnableAsync
public class TreeGraphBaasApp extends SpringBootServletInitializer {
	static {
		int newMaxKeyLength;
		String errorString = "Failed manually overriding key-length permissions.";

		try {
			if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
				System.out.println("will modify aes length");
				Class<?> c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
				Constructor<?> con = c.getDeclaredConstructor();
				con.setAccessible(true);
				Object allPermissionCollection = con.newInstance();
				Field f = c.getDeclaredField("all_allowed");
				f.setAccessible(true);
				f.setBoolean(allPermissionCollection, true);
				c = Class.forName("javax.crypto.CryptoPermissions");
				con = c.getDeclaredConstructor();
				con.setAccessible(true);
				Object allPermissions = con.newInstance();
				f = c.getDeclaredField("perms");
				f.setAccessible(true);
				((Map<String, Object>) f.get(allPermissions)).put("*", allPermissionCollection);
				c = Class.forName("javax.crypto.JceSecurityManager");
				f = c.getDeclaredField("defaultPolicy");
				f.setAccessible(true);
				Field mf = Field.class.getDeclaredField("modifiers");
				mf.setAccessible(true);
				mf.setInt(f, f.getModifiers() & 0xFFFFFFEF);
				f.set(null, allPermissions);
				newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
				System.out.println("newMaxKeyLength:" + newMaxKeyLength);
			}
		} catch (Exception e) {
			throw new RuntimeException(errorString, e);
		}
		if (newMaxKeyLength < 256)
			throw new RuntimeException(errorString);
	}
	
    public static void main(String[] args) {
    	Security.addProvider(new BouncyCastleProvider());
        SpringApplication springApplication = new SpringApplication(TreeGraphBaasApp.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TreeGraphBaasApp.class);
	}
}