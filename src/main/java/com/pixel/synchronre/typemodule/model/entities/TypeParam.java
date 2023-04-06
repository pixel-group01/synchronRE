package com.pixel.synchronre.typemodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Table(name = "type_param") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeParam
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPE_ID_GEN")
    @SequenceGenerator(name = "TYPE_ID_GEN", sequenceName = "TYPE_ID_GEN", allocationSize = 10)
    private Long typeParamId;
    @ManyToOne @JoinColumn(name = "parent_id")
    private Type parent;
    @ManyToOne @JoinColumn(name = "child_id")
    private Type child;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
}