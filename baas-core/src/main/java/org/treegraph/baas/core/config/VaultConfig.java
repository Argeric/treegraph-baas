package org.treegraph.baas.core.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

/**
 *  * 用基于Java的bean元数据注册Spring Vault对象  
 */
@Configuration
public class VaultConfig extends AbstractVaultConfiguration {

	@Value("${vault.token}")
	String token;
	@Value("${vault.uri}")
	URI vaultUri;

	@Override
	public VaultEndpoint vaultEndpoint() {
		return VaultEndpoint.from(vaultUri);
	}

	@Override
	public ClientAuthentication clientAuthentication() {
		return new TokenAuthentication(token);
	}
}