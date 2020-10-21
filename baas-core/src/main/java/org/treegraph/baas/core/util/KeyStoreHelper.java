package org.treegraph.baas.core.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.lang3.StringUtils;
import org.treegraph.baas.core.constant.CharacterConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeyStoreHelper {
	
	public static void createTrustJKSKeyStore(final String originalTrustFolder, final String jksTrustStoreLocation,
			final String password) throws Exception {
		File trustedFolder = new File(originalTrustFolder);
		File[] files = trustedFolder.listFiles();
		if(files == null) {
			return;
		}
		
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(null, password.toCharArray());
		for(File file : files) {
			if(!file.getName().endsWith("crt")) {
				continue;
			}
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) factory.generateCertificate(new FileInputStream(file));
			String certKey = StringUtils.substringAfterLast(originalTrustFolder, CharacterConstant.SLASH);
			keystore.setCertificateEntry(certKey, cert);
			log.info("createTrustJKSKeyStore certKey:{},cert:{}", certKey, file.getName());
		}
		
		FileOutputStream fos = new FileOutputStream(jksTrustStoreLocation);
		keystore.store(fos, password.toCharArray());
		fos.close();
		log.info("createTrustJKSKeyStore finish");
	}
}
