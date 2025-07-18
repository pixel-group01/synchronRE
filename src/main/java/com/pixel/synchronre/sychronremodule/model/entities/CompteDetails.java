package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Table(name = "Compte_Details", uniqueConstraints = @UniqueConstraint(columnNames = {"type_id", "compte_ced_id"}))
public class CompteDetails extends HistoDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_DET_ID_GEN", sequenceName = "COMPTE_DET_ID_GEN")
    private Long compteDetId;
    private BigDecimal debit;
    private BigDecimal credit;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "type_id")
    private Type typeCompteDet;
    String typeCode;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_ced_id")
    private CompteCedante compteCedante;

    public CompteDetails(Long compteDetId) {
        this.compteDetId = compteDetId;
    }

    public CompteDetails(CompteCedante compteCedante) {
        this.compteCedante = compteCedante;
    }

    @Override
    public String toString() {
        return compteDetId +"_"+ typeCompteDet +"_"+ typeCode +"_"+ compteCedante ;
    }
}