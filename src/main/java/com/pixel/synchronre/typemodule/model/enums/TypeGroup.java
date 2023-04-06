package com.pixel.synchronre.typemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum TypeGroup
{
    STRUCTURE("TYP_STR", "TYPE-STRUCTURE"),
    AGENT("TYP_AGT", "TYPE-AGENT"),
    DEMANDE("TYP_DMD", "TYPE-DEMANDE"),
    MOUVEMENT("TYP_MVT", "TYPE-MOUVEMENT"),
    PRIVILEGE("TYP_PRV", "TYPE-PRIVILEGE"),
    ARCHIVE("TYP_ARCH", "TYPE-ARCHIVE"),
    ;
    private String groupCode;
    private String groupName;
}
