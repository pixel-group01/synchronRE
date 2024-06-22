package com.pixel.synchronre.typemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum TypeGroup
{
    TYPE_REP("TYP_REP", "TYPE-REPARTITION"),
    TYPE_AFFAIRE("TYP_AFF", "TYPE-AFFAIRE"),
    TYPE_CED("TYP_CED", "Type de cedante"),
    TYPE_REGLEMENT("TYP_REG", "Type de règlement"),
    TYPE_BORDEREAU("TYP_BORD", "Type de bordereau"),
    MOUVEMENT("TYP_MVT", "TYPE-MOUVEMENT"),
    TYPE_PRV("TYP_PRV", "TYPE-PRIVILEGE"),
    DOCUMENT("TYP_DOC", "TYPE-DOCUMENT"),
    CESSIONNAIRE("TYP_CES", "TYPE-CESSIONNAIRE"), //Cessionnaire, courtier
    MODE_REGLEMENT("TYP_MOD_REG", "MODE_REGLEMENT"),
    TYPE_FUNCTION("TYP_FNC", "Type de fonction"),
    TYPE_PCL("TYPE_PCL", "Type de paramètre de cession légale"),
    TYPE_ASSOCIATION("TYPE_ASSO", "Type de table d'association");
    private String groupCode;
    private String groupName;
}
