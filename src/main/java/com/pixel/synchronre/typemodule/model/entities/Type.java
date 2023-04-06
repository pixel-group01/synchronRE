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
import java.util.List;

@Table(name = "type") @Entity
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

    @Override
    public String toString() {
        return name + " (" +uniqueCode + ")"  ;
    }

    public Type(Long typeId) {
        this.typeId = typeId;
    }
}