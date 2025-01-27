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

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class SousLimite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOUS_LIM_ID_GEN")
    @SequenceGenerator(name = "SOUS_LIM_ID_GEN", sequenceName = "SOUS_LIM_ID_GEN")
    private Long sousLimiteSouscriptionId;
    private BigDecimal sousLimMontant;

    @ManyToOne
    @JoinColumn(name = "cou_id") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Couverture activite;
    @ManyToOne
    @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne
    @JoinColumn(name = "STA_CODE") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Statut statut;
    @ManyToOne
    @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne
    @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return sousLimiteSouscriptionId + "_" + sousLimMontant + "_" + activite;
    }
}
