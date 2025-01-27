package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "v_paiement")
@Getter @Setter
@NoArgsConstructor
public class VPaiement
{
    @Id
    private Long rId;
    private Long affId;
    private Long regId;
    private String dateReglement;
    private Double montantEncaisse;
    private Double commissionTotal;
    private Double commissionCed;
    private Double commissionCourt;
}
