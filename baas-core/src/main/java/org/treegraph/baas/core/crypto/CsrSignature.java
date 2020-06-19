package org.treegraph.baas.core.crypto;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemReader;

public class CsrSignature {

	public static PKCS10CertificationRequest convertPemToPKCS10CertificationRequest(String path) throws Exception {
		PKCS10CertificationRequest csr = null;
		try {
			PemReader pemReader = new PemReader(new FileReader(path + "/consortium.csr"));
			csr = new PKCS10CertificationRequest(pemReader.readPemObject().getContent());
			pemReader.close();
		} catch (UnsupportedEncodingException ex) {
			throw new Exception("UnsupportedEncodingException, convertPemToPublicKey", ex);
		} catch (IOException ex) {
			throw new Exception("IOException, convertPemToPublicKey", ex);
		}
		return csr;
	}

	public static String getX500Field(String asn1ObjectIdentifier, X500Name x500Name) {
		RDN[] rdnArray = x500Name.getRDNs(new ASN1ObjectIdentifier(asn1ObjectIdentifier));
		String retVal = null;
		for (RDN item : rdnArray) {
			retVal = item.getFirst().getValue().toString();
		}
		return retVal;
	}
	
	public static void main(String[] args) throws Exception {
		String filepath = "/Users/wangzhenting/Downloads/consortiumCSR/";
		PKCS10CertificationRequest csr = CsrSignature.convertPemToPKCS10CertificationRequest(filepath);
		X500Name x500Name = csr.getSubject();
		System.out.println("x500Name is: " + x500Name + "\n");
	}
}