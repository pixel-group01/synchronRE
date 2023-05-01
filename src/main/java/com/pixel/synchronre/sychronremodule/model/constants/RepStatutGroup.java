package com.pixel.synchronre.sychronremodule.model.constants;

import java.util.Arrays;
import java.util.List;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

public class RepStatutGroup
{
    public static final List<String> tabSaisie = Arrays.asList(SAISIE_CRT.staCode, ACCEPTE.staCode, RETOURNE.staCode);
    public static final List<String> tabAttenteValidation = Arrays.asList(EN_ATTENTE_DE_VALIDATION.staCode);
    public static final List<String> tabValide = Arrays.asList(VALIDE.staCode, MAIL.staCode);
}
