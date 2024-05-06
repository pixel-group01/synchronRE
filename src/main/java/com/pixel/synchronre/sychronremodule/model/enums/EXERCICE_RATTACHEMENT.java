package com.pixel.synchronre.sychronremodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum EXERCICE_RATTACHEMENT
{

    SURVENANCE("SURVENANCE", "Exercice de survenance"),
    SOUSCRIPTION("SOUSCRIPTION", "Exercice de souscription");
    private String code;
    private String libelle;
}
