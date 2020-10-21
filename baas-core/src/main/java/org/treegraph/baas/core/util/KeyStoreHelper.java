package org.treegraph.baas.core.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;

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
			log.info("createTrustJKSKeyStore file:{}", file.getName());
			if(!file.getName().endsWith("crt")) {
				continue;
			}
			log.info("createTrustJKSKeyStore cert:{}", file.getName());
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) factory.generateCertificate(new FileInputStream(file));
			X500Principal principal = cert.getSubjectX500Principal();
			log.info("createTrustJKSKeyStore principal:{}", principal.getName());
			LdapName ldapDN = new LdapName(principal.getName());
			List<Rdn> rdns = ldapDN.getRdns();
			for(Rdn rdn:rdns) {
				log.info("createTrustJKSKeyStore rdntype:{}, rdnvalue:{}", rdn.getType(), (String)rdn.getValue());
				keystore.setCertificateEntry((String)rdn.getValue(), cert);
			}
		}
		
		FileOutputStream fos = new FileOutputStream(jksTrustStoreLocation);
		keystore.store(fos, password.toCharArray());
		fos.close();
		log.info("createTrustJKSKeyStore finish");
	}
}
