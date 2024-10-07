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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrancheCedante
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CED_TRAI_ID_GEN")
    @SequenceGenerator(name = "CED_TRAI_ID_GEN", sequenceName = "CED_TRAI_ID_GEN")
    private Long trancheCedanteId;
    private BigDecimal pmd;
    private BigDecimal pmdCourtier;
    private BigDecimal pmdCourtierPlaceur;
    private BigDecimal pmdNette;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cedante_traite_id")
    private CedanteTraite cedanteTraite;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tranche_id")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public TrancheCedante(Long trancheCedanteId) {
        this.trancheCedanteId = trancheCedanteId;
    }

    @Override
    public String toString() {
        return "TrancheCedante{" +
                "trancheCedanteId=" + trancheCedanteId +
                ", cedanteTraite=" + cedanteTraite +
                ", tranche=" + tranche +
                '}';
    }
}