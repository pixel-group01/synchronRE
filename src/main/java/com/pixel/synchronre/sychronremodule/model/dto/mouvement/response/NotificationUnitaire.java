package com.pixel.synchronre.sychronremodule.model.dto.mouvement.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationUnitaire
{
    private String label;
    private Long value;
    private boolean forCourtier;
    private boolean forCedante;
    private List<String> prvCodes;
    private List<String> tyfCodes;
}
