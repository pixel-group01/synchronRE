package com.pixel.synchronre.sharedmodule.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatutEnum
{
    SAISIE("SAI", "Saisie"),
    SAISIE_CRT("SAI-CRT", "Saisie courtier"),
    EN_COURS_DE_REPARTITION("CREP", "En cours de répartition"),
    EN_ATTENTE_DE_PLACEMENT("APLA", "En attente de placement"),
    EN_COURS_DE_PLACEMENT("CPLA", "En cours de placement"),
    RETOURNE("RET", "Retournée"),
    EN_ATTENTE_DE_REGLEMENT("AREG", "En attente de règlement"),
    EN_COURS_DE_REGLEMENT("CREG", "En cours de règlement"),
    AFFAIRE_SOLDE("SOLD", "Affaire soldée"),
    EN_ATTENTE_D_ARCHIVAGE("AARC", "En attente d'archivage"),
    ARCHIVE("ARC", "Archivé"),
    TRANSMIS("TRA", "Transmis"),

    EN_ATTENTE_DE_VALIDATION("AVAL", "En attente de validation"),
    VALIDE("VAL", "Validé"),
    EN_ATTENTE_DE_CONFIRMATION("ACONF", "En attente de confirmation"),
    REFUSE("REFUSE", "Réfusé"),
    ANNULE("ANNULE", "Annulé"),
    MODIFIE("MOD", "Modifié"),
    MAIL("MAIL", "Mail envoyé"),
    ACCEPTE("ACCEPTE", "Accepté");

    public String staCode;
    public String staLibelle;
}
