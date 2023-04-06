package com.pixel.synchronre.authmodule.model.dtos.asignation;

import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvAssSpliterDTO
{
    private Set<Long> prvIdsToBeRemoved;
    private Set<Long> prvIdsToBeAddedAsNew;
    private Set<Long> prvIdsToChangeTheDates;
    private Set<Long> prvIdsToActivateAndChangeTheDates;
    private Set<Long> prvIdsToActivate;
}
