package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class TrancheCedante //extends HistoDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CED_TRAI_ID_GEN")
    @SequenceGenerator(name = "CED_TRAI_ID_GEN", sequenceName = "CED_TRAI_ID_GEN")
    private Long trancheCedanteId;
    @Column(precision = 50, scale = 20)
    private BigDecimal assiettePrime;
    @Column(precision = 50, scale = 20)
    private BigDecimal assiettePrimeRealsee;
    @Column(precision = 50, scale = 20)
    private BigDecimal pmd;
    @Column(precision = 50, scale = 20)
    private BigDecimal pmdCourtier;
    @Column(precision = 50, scale = 20)
    private BigDecimal pmdCourtierPlaceur;
    @Column(precision = 50, scale = 20)
    private BigDecimal pmdNette;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ced_id") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Cedante cedante;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tranche_id")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;


    public TrancheCedante(Long trancheCedanteId) {
        this.trancheCedanteId = trancheCedanteId;
    }

    @Override
    public String toString() {
        return trancheCedanteId + "_" + cedante + "_" + tranche;
    }
}
