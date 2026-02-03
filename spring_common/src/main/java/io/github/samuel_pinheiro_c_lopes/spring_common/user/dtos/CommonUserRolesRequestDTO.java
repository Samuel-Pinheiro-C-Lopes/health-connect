package io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonUserRolesRequestDTO {

    @JsonProperty("roles")
    private List<String> roles;

    public CommonUserRolesRequestDTO() {
    }

    // Constructor for convenience (since you create this manually in code)
    public CommonUserRolesRequestDTO(List<String> roles) {
        this.roles = roles;
    }

    // Interface method match
    public List<String> roles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}