package org.treegraph.baas.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
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
		Assert.isTrue(alg != null, "only support[rsa  sm  ecdsa]");
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
		if (algo.equals("ec")) {
			keyPair = this.generateKeyService.generateEncryptedEcPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (algo.equals("sm")) {
			keyPair = this.generateKeyService.generateEncryptedSmPrivateKey(password, pkcsType, byteArrayOutputStream);
		} else if (algo.equals("rsa")) {
			keyPair = this.generateKeyService.generateEncryptedRsaPrivateKey(password, pkcsType, byteArrayOutputStream);
		}
		zipOutputStream.write(byteArrayOutputStream.toByteArray());
		zipOutputStream.closeEntry();
		byteArrayOutputStream.close();

		// public key
		zipOutputStream.putNextEntry(new ZipEntry("public.keystore"));
		if (algo.equals("ec") || algo.equals("sm")) {
			zipOutputStream.write(this.generateKeyService.getECPublicKeyString(keyPair).getBytes());
		} else if (algo.equals("rsa")) {
			zipOutputStream.write(this.generateKeyService.getRsaPublicKeyString(keyPair).getBytes());
		}
		zipOutputStream.closeEntry();
		zipOutputStream.close();
	}
}