package io.github.samuel_pinheiro_c_lopes.userservice.configuration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.security.roles")
public class RolesConfiguration {
	private static final String PREFIX = "";//"ROLE_";
	private String admin;
	private String user;
	private String manager;
	
	public String getAdmin() {
		return PREFIX + admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getUser() {
		return PREFIX + user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getManager() {
		return PREFIX + manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
}
