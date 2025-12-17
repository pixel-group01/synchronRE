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

/**
 * Mapping for DB view v_sous_limite
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_sous_limite")
public class VSousLimite {

    @Id
    @Column(name = "sous_limite_souscription_id")
    private Long sousLimiteSouscriptionId;

    @Column(name = "cou_libelles", length = Integer.MAX_VALUE)
    private String couLibelles;

    @Column(name = "sous_lim_montant", precision = 38, scale = 2)
    private BigDecimal sousLimMontant;

    @Column(name = "traite_np_id")
    private Long traiteNpId;

}
