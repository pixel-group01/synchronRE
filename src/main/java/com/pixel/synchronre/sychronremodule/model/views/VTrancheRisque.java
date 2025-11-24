package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_tranche_risque")
public class VTrancheRisque
{
    @Id
    @Column(name = "r_id")
    private Long rId;

    @Column(name = "tranche_id")
    private Long trancheId;

    @Column(name = "tranche_numero")
    private Integer trancheNumero;

    @Size(max = 255)
    @Column(name = "tranche_type")
    private String trancheType;

    @Size(max = 255)
    @Column(name = "tranche_libelle")
    private String trancheLibelle;

    @Column(name = "tranche_priorite", precision = 38, scale = 2)
    private BigDecimal tranchePriorite;

    @Column(name = "tranche_porte", precision = 38, scale = 2)
    private BigDecimal tranchePorte;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "traite_np_id")
    private Long traiteNpId;

    @Column(name = "risque_id")
    private Long risqueId;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 4000)
    @Column(name = "cou_libelle", length = 4000)
    private String couLibelle;

    @Column(name = "cou_id")
    private Long couId;

    @Column(name = "asso_type_id")
    private Long assoTypeId;

    @Size(max = 255)
    @Column(name = "asso_type_unique_code")
    private String assoTypeUniqueCode;

    @Column(name = "tranche_taux_prime", precision = 38, scale = 2)
    private BigDecimal trancheTauxPrime;

}