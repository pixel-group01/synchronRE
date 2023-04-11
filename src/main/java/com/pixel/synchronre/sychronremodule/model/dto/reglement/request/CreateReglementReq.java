package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateReglementReq {
    private Long regId;
    private String regCode;
    private String regReference;
    private LocalDate regDate;
    private float regMontant;
    private float regCommission;
    private String typeReglement;
    private Long affId;
    private Long userId;
}
