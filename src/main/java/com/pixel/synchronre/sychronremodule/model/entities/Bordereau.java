package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Bordereau
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BORD_ID_GEN")
    @SequenceGenerator(name = "BORD_ID_GEN", sequenceName = "BORD_ID_GEN")
    private Long bordId;
    @Column(unique = true)
    private String bordNum;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "rep_id")
    private Repartition repartition;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "aff_id")
    private Affaire affaire;
    private BigDecimal bordMontantTotalPrime;
    private BigDecimal bordMontantTotalCommission;
    private BigDecimal bordMontantTotalPrimeAreverser;
    private String bordMontantTotalPrimeAreverserLette;
    private LocalDate brodDateLimite;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "type_code") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "bord_statut") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Statut statut;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @Override
    public String toString() {
        return bordId + "_" + bordNum ;
    }
}
