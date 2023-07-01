package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateInitialFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.CreateFunctionDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreaterUserAndFunctionDTO
{
    @Valid
    private CreateUserDTO createUserDTO;
    @Valid
    private List<CreateInitialFncDTO> createInitialFncDTO;
}
