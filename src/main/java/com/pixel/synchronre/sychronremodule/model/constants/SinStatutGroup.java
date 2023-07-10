package com.pixel.synchronre.sychronremodule.model.constants;

import java.util.Arrays;
import java.util.List;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

public class SinStatutGroup
{
    public static final List<String> tabSaisie = Arrays.asList(SAISIE.staCode,RETOURNE.staCode);
    public static final List<String> tabAtrans = Arrays.asList(SAISIE_CRT.staCode,TRANSMIS.staCode,RETOURNE.staCode,RETOURNER_VALIDATEUR.staCode);
    public static final List<String> tabAValidation = Arrays.asList(EN_ATTENTE_DE_VALIDATION.staCode,RETOURNER_COMPTABLE.staCode);
    public static final List<String> tabEnReglement = Arrays.asList(VALIDE.staCode,EN_ATTENTE_DE_PAIEMENT.staCode, EN_COURS_DE_PAIEMENT.staCode,EN_COURS_DE_REVERSEMENT.staCode,EN_COURS_DE_PAIEMENT_REVERSEMENT.staCode);
    public static final List<String> tabSolde = Arrays.asList(SOLDE.staCode);
    public static final List<String> tabArchive = Arrays.asList(ARCHIVE.staCode);
    public static final List<String> tabAllSinistres = Arrays.asList(
            SAISIE.staCode,SAISIE_CRT.staCode,RETOURNE.staCode,
            TRANSMIS.staCode,RETOURNE.staCode,RETOURNER_VALIDATEUR.staCode,
            EN_ATTENTE_DE_VALIDATION.staCode,RETOURNER_COMPTABLE.staCode,
            EN_ATTENTE_DE_PAIEMENT.staCode, EN_COURS_DE_PAIEMENT.staCode,
            EN_COURS_DE_REVERSEMENT.staCode,EN_COURS_DE_PAIEMENT_REVERSEMENT.staCode
    );
}
