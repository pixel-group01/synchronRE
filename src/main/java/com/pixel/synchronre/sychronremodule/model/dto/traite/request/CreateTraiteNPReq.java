package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNumero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateTraiteNPReq
{
    @NotNull(message = "La référence unique est obligatoire")
    @NotBlank(message = "La référence unique est obligatoire")
    @UniqueTraiteNpRef
    private String traiReference;
    @UniqueTraiteNumero
    private String traiNumero;
    private String traiLibelle;
    private String traiAuteur;
    private String traiEcerciceRattachement;
    private LocalDate traiDateEffet;
    private LocalDate traiDateEcheance;
    private BigDecimal traiCoursDevise;
    private String traiPeriodicite;
    private Long traiDelaiEnvoi;
    private Long traiDelaiConfirmation;
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxCourtierPlaceur;
    private Long exeCode;
    private String traiSourceRef;
    private String natCode;
    private String devCode;
    private String traiCompteDevCode;
}
