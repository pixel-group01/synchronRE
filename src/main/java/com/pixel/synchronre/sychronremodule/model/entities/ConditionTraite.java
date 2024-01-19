package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class ConditionTraite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COND_TRAITE_ID_GEN")
    @SequenceGenerator(name = "COND_TRAITE_ID_GEN", sequenceName = "COND_TRAITE_ID_GEN")
    private Long conditionId;
    private boolean conditionExclusion;
    @ManyToOne @JoinColumn(name = "TRAITE_ID")
    private Traite traite;
    @ManyToOne @JoinColumn(name = "CED_ID")
    private Cedante cedante;
    @ManyToOne @JoinColumn(name = "COU_ID")
    private Couverture couverture;
    @ManyToOne @JoinColumn(name = "TYPE_ID")
    private Type typeCondition;
}