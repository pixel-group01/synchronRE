package com.pixel.synchronre.sychronremodule.model.dto.mouvement.request;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.validators.OnMvtRetour;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MvtPlacementReq
{
    private Long plaId;
    private String staCode;
    private MultipartFile file;
    @NotNull(groups = {OnMvtRetour.class})
    private String motif;
}
