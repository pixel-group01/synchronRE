package com.pixel.synchronre.authmodule.model.dtos.asignation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoleAssSpliterDTO
{
    private Set<Long> roleIdsToBeRemoved;
    private Set<Long> roleIdsToBeAddedAsNew;
    private Set<Long> roleIdsToChangeTheDates;
    private Set<Long> roleIdsToActivateAndChangeTheDates;
    private Set<Long> roleIdsToActivate;
}
