package io.github.samuel_pinheiro_c_lopes.spring_common.security.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.security.roles")
public class RolesConfiguration {
	private static final String PREFIX = "";
	private String admin;
	private String patient;
	private String doctor;
	
	public String getAdmin() {
		return PREFIX + admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getPatient() {
		return PREFIX + patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}
	public String getDoctor() {
		return PREFIX + doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
}
