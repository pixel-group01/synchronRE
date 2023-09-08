package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pixel.synchronre.typemodule.model.dtos.ExistingTypeId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateInitialFncDTO
{
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate startsAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExistingTypeId
    @NotNull(message = "Veuillez selectionner le type de la fonction")
    private Long typeFunctionId;
    protected LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}
