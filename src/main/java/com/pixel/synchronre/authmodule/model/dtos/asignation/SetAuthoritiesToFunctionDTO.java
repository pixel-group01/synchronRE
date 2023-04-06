package com.pixel.synchronre.authmodule.model.dtos.asignation;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SetAuthoritiesToFunctionDTO
{
    @ExistingAssId
    private Long fncId;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<Long> roleIds = new HashSet<>();
    private Set<Long> prvIds = new HashSet<>();
}