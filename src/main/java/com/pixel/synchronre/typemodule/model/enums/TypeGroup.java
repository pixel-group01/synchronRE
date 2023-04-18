package com.pixel.synchronre.typemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum TypeGroup
{
    TYPE_REP("TYP_REP", "TYPE-REPARTITION"),
    TYPE_AFFAIRE("TYP_AFF", "TYPE-AFFAIRE"),
    TYPE_CED("TYP_CED", "Type de cedante"),
    AGENT("TYP_AGT", "TYPE-AGENT"),
    DEMANDE("TYP_DMD", "TYPE-DEMANDE"),
    MOUVEMENT("TYP_MVT", "TYPE-MOUVEMENT"),
    TYPE_PRV("TYP_PRV", "TYPE-PRIVILEGE"),
    ARCHIVE("TYP_ARCH", "TYPE-ARCHIVE");
    private String groupCode;
    private String groupName;
}
