package com.pixel.synchronre.authmodule.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppRole
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID_GEN")
    @SequenceGenerator(name = "ROLE_ID_GEN", sequenceName = "ROLE_ID_GEN", allocationSize = 10)
    private Long roleId;
    @Column(unique = true)
    private String roleCode;
    @Column(unique = true)
    private String roleName;
    private String prvAuthorizedTypes;

    public AppRole(Long roleId, String roleCode, String roleName) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public AppRole(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppRole)) return false;
        AppRole appRole = (AppRole) o;
        return Objects.equals(getRoleId(), appRole.getRoleId()) || Objects.equals(getRoleCode(), appRole.getRoleCode()) || Objects.equals(getRoleName(), appRole.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRoleCode(), getRoleName());
    }

    @Override
    public String toString() {
        return roleId + "_" + roleCode + "_"+ roleName ;
    }
}
