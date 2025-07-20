package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_periode")
public class VPeriode
{
    @Id
    private Long periodeId;
    private Integer numPeriode;
    private Integer annee;
    private Long typeId;
    private LocalDate periode;
    private String periodeName;
    private String periodicite;
}
