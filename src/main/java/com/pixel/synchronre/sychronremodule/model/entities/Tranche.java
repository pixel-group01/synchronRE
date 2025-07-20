package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tranche
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANCHE_ID_GEN")
    @SequenceGenerator(name = "TRANCHE_ID_GEN", sequenceName = "TRANCHE_ID_GEN")
    private Long trancheId;
    private String trancheType;
    private String trancheLibelle;
    private BigDecimal tranchePriorite;
    private BigDecimal tranchePorte;
    private BigDecimal trancheTauxPrime;
    private Long trancheNumero;
    @ManyToOne @JoinColumn(name = "risque_couvert_ID")
    private RisqueCouvert risqueCouvert;

    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;

    @ManyToOne @JoinColumn(name = "STA_CODE") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return trancheId +"_" + trancheType + "_" + trancheLibelle + "_" + trancheTauxPrime;
    }

    public Tranche(Long trancheId) {
        this.trancheId = trancheId;
    }

    public Tranche(Long trancheId, String trancheLibelle, BigDecimal trancheTauxPrime)
    {
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
        this.trancheTauxPrime = trancheTauxPrime;
    }
}
