package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class CompteCedante
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_CED_ID_GEN", sequenceName = "COMPTE_CED_ID_GEN")
    private Long compteCedId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_id")
    private Compte compte;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ced_id") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Cedante cedante;

    public CompteCedante(Compte compte, Cedante cedante) {
        this.compte = compte;
        this.cedante = cedante;
    }

    public CompteCedante(Long compteCedId) {
        this.compteCedId = compteCedId;
    }

    @Override
    public String toString() {
        return compteCedId +"_"+ compte +"_"+ cedante;
    }
}