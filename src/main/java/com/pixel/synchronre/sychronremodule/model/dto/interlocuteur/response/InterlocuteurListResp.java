package com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InterlocuteurListResp
{
    private Long intId;
    private String intNom;
    private String intPrenom;
    private String intTel;
    private String intEmail;
    private Long intCesId;
    private String cesNom;
    private String cesSigle;
    private String statut;

    public InterlocuteurListResp(Long intId, String intNom, String intPrenom, String intTel, String intEmail, Long intCesId, String cesNom, String cesSigle, String statut) {
        this.intId = intId;
        this.intNom = intNom;
        this.intPrenom = intPrenom;
        this.intTel = intTel;
        this.intEmail = intEmail;
        this.intCesId = intCesId;
        this.cesNom = cesNom;
        this.cesSigle = cesSigle;
        this.statut = statut;
    }

    private boolean isSelected;
    private boolean isPrincipal;
}
