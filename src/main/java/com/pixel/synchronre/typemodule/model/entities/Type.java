package com.pixel.synchronre.typemodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.List;

@Table(name = "type") @Entity  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPE_ID_GEN")
    @SequenceGenerator(name = "TYPE_ID_GEN", sequenceName = "TYPE_ID_GEN", allocationSize = 10)
    private  Long typeId;
    @Enumerated(EnumType.STRING)
    private TypeGroup typeGroup;
    @Column(nullable = false, unique = true)
    private String uniqueCode;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
    @Transient
    private List<Type> children;
    private String objectFolder;
    private int typeOrdre = 0;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean debitDisabled;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean creditDisabled;

    public Type(Long typeId, TypeGroup typeGroup, String uniqueCode, String name, PersStatus status, List<Type> children, String objectFolder) {
        this.typeId = typeId;
        this.typeGroup = typeGroup;
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.status = status;
        this.children = children;
        this.objectFolder = objectFolder;
    }



    @Override
    public String toString() {
        return name + " (" +uniqueCode + ")"  ;
    }

    public Type(Long typeId) {
        this.typeId = typeId;
    }
}
