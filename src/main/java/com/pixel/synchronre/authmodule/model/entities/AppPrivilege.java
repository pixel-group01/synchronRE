package com.pixel.synchronre.authmodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppPrivilege
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRV_ID_GEN")
    @SequenceGenerator(name = "PRV_ID_GEN", sequenceName = "PRV_ID_GEN", allocationSize = 10)
    private Long privilegeId;
    @Column(unique = true, columnDefinition = "text")
    private String privilegeCode;
    @Column(unique = true, columnDefinition = "text")
    private String privilegeName;
    @ManyToOne @JoinColumn
    private Type prvType;

    public AppPrivilege(Long privilegeId, String privilegeCode) {
        this.privilegeId = privilegeId;
        this.privilegeCode = privilegeCode;
    }

    public AppPrivilege(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o==null) return false;
        if(!(o instanceof AppPrivilege)) return false;
        AppPrivilege that = (AppPrivilege) o;
        return privilegeId.equals(that.privilegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privilegeId);
    }

    @Override
    public String toString() {
        return privilegeId + "_"+ privilegeCode + "_" + prvType;
    }
}
