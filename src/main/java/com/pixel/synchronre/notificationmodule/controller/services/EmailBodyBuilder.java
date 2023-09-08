package com.pixel.synchronre.notificationmodule.controller.services;

import java.math.BigDecimal;
import java.util.Map;

public interface EmailBodyBuilder
{
    Map<String, String> buildTransmissionAffaireBody(String nomDestinataire, String nomCedante);
    Map<String, String>  buildRetourAffaireBody(String nomDestinataire, String affCode, String motif);

    Map<String, String> buildAffaireAttenteReglementBody(String nomDestinataire);

    Map<String, String> buildPlacementEnAttenteDeValidationBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise);

    Map<String, String> buildPlacementRetourneBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise, String motif);

    Map<String, String> buildPlacementEnAttenteDEnvoieDeNoteDeCessionnBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise);
}
/*
Objet : Retour de votre affaire N°affCode
Bonjour destinataire.
Votre affaire N°affCode a été retournée par Nelson-RE pour les motif suivant : \n
moti \n
Veuillez accéder à Synchron-RE pour traiter la requête

Cordialement


Objet :  Nouvelle affaire de NSIA assurance Cote d'Ivoire

Bonjour YOUIN,

Vous venez de recevoir une nouvelle affaire de la cedante NSIA assurance Cote d'ivoire.
Veuillez acceder sychronRE pour traiter la requette.

Cordialement.

Objet :  Nouvelle affaire en attente de règlement

Bonjour YOUIN,

Vous venez de recevoir une nouvelle affaire en attente de règlement.
Vous pouvez acceder à la plateforme sychronRE pour d'éventuel(s) règlement(s) sur cette affaire.

Cordialement.


 */