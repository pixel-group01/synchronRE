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
@Table(name = "v_tranche")
public class VTranche
{
    @Id
    @Column(name = "tranche_id")
    private Long trancheId;

    @Size(max = 255)
    @Column(name = "tranche_type")
    private String trancheType;

    @Column(name = "tranche_numero")
    private Integer trancheNumero;

    @Column(name = "tranche_porte", precision = 38, scale = 2)
    private BigDecimal tranchePorte;

    @Column(name = "tranche_priorite", precision = 38, scale = 2)
    private BigDecimal tranchePriorite;

    @Size(max = 255)
    @Column(name = "tranche_libelle")
    private String trancheLibelle;

    @Column(name = "traite_np_id")
    private Long traiteNpId;

    @Column(name = "couvertures", length = Integer.MAX_VALUE)
    private String couvertures;

    @Column(name = "cou_ids", length = Integer.MAX_VALUE)
    private String couIds;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "tranche_taux_prime", precision = 38, scale = 2)
    private BigDecimal trancheTauxPrime;

    @Size(max = 255)
    @Column(name = "sta_code")
    private String staCode;

    @Column(name = "search_text", length = Integer.MAX_VALUE)
    private String searchText;

    @Size(max = 255)
    @Column(name = "trai_reference")
    private String traiReference;

    @Size(max = 255)
    @Column(name = "trai_numero")
    private String traiNumero;

    @Column(name = "risque_ids", length = Integer.MAX_VALUE)
    private String risqueIds;

}