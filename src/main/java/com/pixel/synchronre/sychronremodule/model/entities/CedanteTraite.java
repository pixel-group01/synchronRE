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
public class CedanteTraite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CED_TRAI_ID_GEN")
    @SequenceGenerator(name = "CED_TRAI_ID_GEN", sequenceName = "CED_TRAI_ID_GEN")
    private Long cedanteTraiteId;
    private BigDecimal assiettePrime;
    private BigDecimal tauxPrime;
    private BigDecimal pmd;
    @ManyToOne @JoinColumn(name = "ced_id")
    private Cedante cedante;
    @ManyToOne @JoinColumn(name = "traite_np_reference")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
