package com.pixel.synchronre.sychronremodule.model.dto.cedente;

import com.pixel.synchronre.sychronremodule.model.dto.cedente.validator.UniqueCedEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.validator.UniqueCedTel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadCedenteDTO
{
    private Long cedId;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private String cedTel;
    private String cedEmail;
    private String cedAdressePostale;
    private String cedFax;
    private String cedSituationGeo;
    private String cedStatut;
}
