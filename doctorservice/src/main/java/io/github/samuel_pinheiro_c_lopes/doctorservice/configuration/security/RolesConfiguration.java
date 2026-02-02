package io.github.samuel_pinheiro_c_lopes.doctorservice.configuration.security;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "app.security.roles")
public class RolesConfiguration {
    private static final String PREFIX = ""; 
    private String admin;
    private String user;
    private String manager;
    private String doctor;
    public String getAdmin() {
        return PREFIX + admin;
    }
    public void setAdmin(final String admin) {
        this.admin = admin;
    }

    public String getUser() {
        return PREFIX + user;
    }
    public void setUser(final String user) {
        this.user = user;
    }

    public String getManager() {
        return PREFIX + manager;
    }
    public void setManager(final String manager) {
        this.manager = manager;
    }

    public String getDoctor() {
        return PREFIX + doctor;
    }
    public void setDoctor(final String doctor) {
        this.doctor = doctor;
    }
}