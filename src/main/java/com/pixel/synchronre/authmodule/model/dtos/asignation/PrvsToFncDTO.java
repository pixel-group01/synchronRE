package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.ExistingFncId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrvsToFncDTO
{
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> prvIds = new HashSet<>();
    @ExistingFncId
    private Long fncId;
}
