package com.pixel.synchronre.sychronremodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum PERIODICITE
{

    ANNUELLE("ANNUELLE", "Annuelle"),
    SEMESTRIELLE("SEMESTRIELLE", "Semestrielle"),
    TRIMESTRIELLE("TRIMESTRIELLE", "Trimestrielle"),
    MENSUELLE("MENSUELLE", "Mensuelle");
    private String code;
    private String libelle;
}
