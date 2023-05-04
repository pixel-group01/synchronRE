package com.pixel.synchronre.sychronremodule.model.constants;

import java.util.Arrays;
import java.util.List;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

public class AffStatutGroup
{
    public static final List<String> tabSaisieCed = Arrays.asList(SAISIE.staCode, SAISIE_CRT.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode);
    public static final List<String> tabEnCoursPla = Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode);
    public static final List<String> tabEnCoursReg = Arrays.asList(EN_ATTENTE_DE_REGLEMENT.staCode, EN_COURS_DE_REGLEMENT.staCode);
    public List<String> tabEnCoursRegSous = Arrays.asList(EN_ATTENTE_DE_REGLEMENT.staCode, EN_COURS_DE_REGLEMENT.staCode);
    public static final List<String> tabSaisieSous = Arrays.asList(SAISIE_CRT.staCode, TRANSMIS.staCode);
    public static final List<String> tabArchives = Arrays.asList(EN_ATTENTE_DE_VALIDATION.staCode, VALIDE.staCode, MAIL.staCode);
    public static final List<String> tabAllAffaires = Arrays.asList(SAISIE.staCode,RETOURNE.staCode,EN_ATTENTE_DE_VALIDATION.staCode,EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode,
                                                                    EN_COURS_DE_REPARTITION.staCode,VALIDE.staCode, MAIL.staCode,SAISIE_CRT.staCode, TRANSMIS.staCode,
                                                                    EN_ATTENTE_DE_REGLEMENT.staCode, EN_COURS_DE_REGLEMENT.staCode
                                                                    );
}
