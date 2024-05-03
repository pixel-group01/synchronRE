package com.pixel.synchronre.sychronremodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum FORME
{
    NPROP("NPROP", "Non proportionnel"),
    PROP("PROP", "Proportionnel");
    private String formeCode;
    private String formeLibelle;
}
