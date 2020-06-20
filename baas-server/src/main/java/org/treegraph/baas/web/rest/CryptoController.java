package org.treegraph.baas.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.time.temporal.ChronoUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.core.enums.CryptoEnum.Algo;
import org.treegraph.baas.core.enums.CryptoEnum.Pkcs;
import org.treegraph.baas.core.service.impl.GenerateKeyService;
import org.treegraph.baas.core.util.LogSkip;

import io.jsonwebtoken.lang.Assert;

@RestController
@RequestMapping({ "/api/crypto" })
public class CryptoController {
	@Autowired
	private GenerateKeyService generateKeyService;

	@LogSkip
	@RequestMapping({ "/generateNodeKey" })
	public void generateNodeKey(String algo, String pkcs, String password, HttpServletResponse response)
			throws Exception {
		// check
		Algo alg = Algo.getByCode(algo);
		Assert.isTrue(alg != null, "only support[ecdsa ec rsa]");
		Pkcs pkcsType = Pkcs.getByCode(pkcs);
		if (pkcsType != null && !GenerateKeyService.checkPassword(password)) {
			throw new RuntimeException("password is too simple");
		}

		// set header
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=\"nodekey.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) response.getOutputStream());

		// private key
		zipOutputStream.putNextEntry(new ZipEntry("private.keystore"));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		KeyPair keyPair = null;
		if (Algo.ECC.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedEcPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.SM2.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedSmPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.RSA.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedRsaPrivateKey(password, pkcsType, byteArrayOutputStream);
		}
		zipOutputStream.write(byteArrayOutputStream.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream.close();

		// public key
		zipOutputStream.putNextEntry(new ZipEntry("public.keystore"));
		if (Algo.ECC.getCode().equals(algo) || Algo.SM2.getCode().equals(algo)) {
			zipOutputStream.write(generateKeyService.getECPublicKeyString(keyPair).getBytes());
		} else if (Algo.RSA.getCode().equals(algo)) {
			zipOutputStream.write(generateKeyService.getRsaPublicKeyString(keyPair).getBytes());
		}
		zipOutputStream.closeEntry();
		zipOutputStream.close();
	}
	
	@LogSkip
	@RequestMapping({ "/generateRootCA" })
	public void generateRootCA(String algo, String pkcs, String password, HttpServletResponse response)
			throws Exception {
		// check
		Algo alg = Algo.getByCode(algo);
		Assert.isTrue(alg != null, "only support[ecdsa ec rsa]");
		Pkcs pkcsType = Pkcs.getByCode(pkcs);
		if (pkcsType != null && !GenerateKeyService.checkPassword(password)) {
			throw new RuntimeException("password is too simple");
		}

		// set header
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=\"rootCA.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) response.getOutputStream());

		// private key
		zipOutputStream.putNextEntry(new ZipEntry("private.keystore"));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		KeyPair keyPair = null;
		if (Algo.ECC.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedEcPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.SM2.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedSmPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.RSA.getCode().equals(algo)) {
			keyPair = generateKeyService.generateEncryptedRsaPrivateKey(password, pkcsType, byteArrayOutputStream);
		}
		zipOutputStream.write(byteArrayOutputStream.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream.close();

		// public key
		zipOutputStream.putNextEntry(new ZipEntry("public.keystore"));
		if (Algo.ECC.getCode().equals(algo) || Algo.SM2.getCode().equals(algo)) {
			zipOutputStream.write(generateKeyService.getECPublicKeyString(keyPair).getBytes());
		} else if (Algo.RSA.getCode().equals(algo)) {
			zipOutputStream.write(generateKeyService.getRsaPublicKeyString(keyPair).getBytes());
		}
		zipOutputStream.closeEntry();
		
		// rootCA
		zipOutputStream.putNextEntry(new ZipEntry("rootCA.cer"));
		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
		generateKeyService.generateRootCA(alg, 1, ChronoUnit.YEARS, keyPair, byteArrayOutputStream1);
		zipOutputStream.write(byteArrayOutputStream1.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream1.close();
		zipOutputStream.close();
	}
	
	@LogSkip
	@RequestMapping({ "/generateConsortiumCSR" })
	public void generateConsortiumCSR(String algo, String pkcs, String password, HttpServletResponse response)
			throws Exception {
		// check
		Algo alg = Algo.getByCode(algo);
		Assert.isTrue(alg != null, "only support[ecdsa ec rsa]");
		Pkcs pkcsType = Pkcs.getByCode(pkcs);
		if (pkcsType != null && !GenerateKeyService.checkPassword(password)) {
			throw new RuntimeException("password is too simple");
		}

		// set header
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=\"consortiumCSR.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) response.getOutputStream());

		// private key
		zipOutputStream.putNextEntry(new ZipEntry("private.keystore"));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		KeyPair keyPair = null;
		if (Algo.ECC == alg) {
			keyPair = generateKeyService.generateEncryptedEcPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.SM2 == alg) {
			keyPair = generateKeyService.generateEncryptedSmPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (Algo.RSA == alg) {
			keyPair = generateKeyService.generateEncryptedRsaPrivateKey(password, pkcsType, byteArrayOutputStream);
		}
		zipOutputStream.write(byteArrayOutputStream.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream.close();

		// public key
		zipOutputStream.putNextEntry(new ZipEntry("public.keystore"));
		if (Algo.ECC == alg || Algo.SM2 == alg) {
			zipOutputStream.write(generateKeyService.getECPublicKeyString(keyPair).getBytes());
		} else if (Algo.RSA == alg) {
			zipOutputStream.write(generateKeyService.getRsaPublicKeyString(keyPair).getBytes());
		}
		zipOutputStream.closeEntry();
		
		// rootCA
		zipOutputStream.putNextEntry(new ZipEntry("consortium.csr"));
		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
		generateKeyService.generateConsortiumCSR(alg, keyPair, byteArrayOutputStream1);
		zipOutputStream.write(byteArrayOutputStream1.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream1.close();
		zipOutputStream.close();
	}
}