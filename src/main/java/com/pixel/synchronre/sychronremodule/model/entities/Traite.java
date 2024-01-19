package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Traite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAITE_ID_GEN")
    @SequenceGenerator(name = "TRAITE_ID_GEN", sequenceName = "TRAITE_ID_GEN")
    private Long tariteId;
    private String traiteCode;
    private String traiteLibelle;
    private BigDecimal traiteTaux;
    private BigDecimal smpMax;
    private BigDecimal smpMin;
    private BigDecimal capitalMax;
    private BigDecimal capitalMin;
    private LocalDate dateEffet;
    private LocalDate dateEcheance;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "TYPE_ID")
    private Type typeTraite;
    @Transient
    private List<ConditionTraite> conditions;

    public Traite(Long tariteId) {
        this.tariteId = tariteId;
    }
}
/*
    Est-il possible que tous les traites sensés s'appliquer sur une affaire ne puissent pas s'appliquer du fait que certains traités est absorbé tout le capital de l'affaire ?
    Comment sont libellées les conditions sur un traité ?
    Existe t-il des condition d'exclusion sur les traités ? C'est à dire des conditions qui lorsqu'elles se réalisent sur affaire conduisent systématiquement à la non application du traité sur l'affaire
*/