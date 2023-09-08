package com.pixel.synchronre.notificationmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component @RequiredArgsConstructor
public class EmailBodyBuilderImpl implements EmailBodyBuilder
{
    @Override
    public Map<String, String> buildTransmissionAffaireBody(String nomDestinataire, String nomCedante)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle demande de placement de " + nomCedante;
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "Vous venez de recevoir une nouvelle demande de placement de la part de %s assurance Cote d'ivoire.\n"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.\n"
                        + "Cordialement.",
                nomDestinataire,
                nomCedante,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public  Map<String, String> buildRetourAffaireBody(String nomDestinataire, String affCode, String motif)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Retour de votre affaire N°" + affCode;
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "Votre affaire N°%s a été retournée par Nelson-RE pour le(s) motif(s) suivant(s) : \n"
                        +"%s\n"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.\n"
                        + "Cordialement.",
                nomDestinataire,
                affCode,
                motif,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public  Map<String, String> buildAffaireAttenteReglementBody(String nomDestinataire)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle affaire en attente de règlement";
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "Vous venez de recevoir une nouvelle affaire en attente de règlement.\n" +
                        "Vous pouvez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour d'éventuel(s) règlement(s) sur cette affaire.\n" +
                        "\n" +
                        "Cordialement.",
                nomDestinataire,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementEnAttenteDeValidationBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle proposition de placement en attente de validation";
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "Vous venez de recevoir une nouvelle proposition de placement en attente de validation.\n" +
                        "Il s'agit d'un placement sur l'affaire N°%s, auprès du cessionnaire %s, pour un montant de %d %s"+
                        "Vous pouvez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour le traitement de ce placement.\n" +
                        "\n" +
                        "Cordialement.",
                nomDestinataire, affCode, cesNom, repCapital, devise,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementRetourneBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise, String motif) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Proposition de placement retournée par le validateur";
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "La proposition de placement sur l'affaire N°%s a auprès du cessionnaire %s, d'un montant de %d %s a été retournée par le validateur.\n" +
                        "Le(s) motif(s) de ce retour est le suivant :\n " +
                        "<b>%s</b>"+
                        "Vous pouvez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour d'éventuel(s) traitements sur ce placement.\n" +
                        "\n" +
                        "Cordialement.",
                nomDestinataire, affCode, cesNom, repCapital, devise, motif,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementEnAttenteDEnvoieDeNoteDeCessionnBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Proposition de placement validée";
        String corps = String.format(
                "Bonjour %s,\n\n"
                        + "La proposition de placement sur l'affaire N°%s a auprès du cessionnaire %s, d'un montant de %d %s a été validée.\n" +
                        "<b>%s</b>"+
                        "Vous pouvez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour l'envoie de la note de cession au cessionnaire.\n" +
                        "\n" +
                        "Cordialement.",
                nomDestinataire, affCode, cesNom, repCapital, devise,
                "http://164.160.41.153/Synchronre-Web/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }
}
/*
Objet :  Nouvelle demande de placement de $NSIA assurance Cote d'Ivoire

Bonjour $YOUIN,

Vous venez de recevoir une nouvelle demande de placement de la part de $NSIA assurance Cote d'ivoire.
Veuillez accéder à la plateforme <sychronRE> pour traiter la requête.
Cordialement.

 */
/*
Objet : Retour de votre affaire N°affCode
Bonjour destinataire.
Votre affaire N°affCode a été retournée par Nelson-RE pour le(s) motif(s) suivant(s) : \n
motif \n
Veuillez accéder à Synchron-RE pour traiter la requête

Cordialement
 */
/*
Objet :  Nouvelle affaire en attente de règlement

Bonjour YOUIN,

Vous venez de recevoir une nouvelle affaire en attente de règlement.
Vous pouvez acceder à la plateforme sychronRE pour d'éventuel(s) règlement(s) sur cette affaire.

Cordialement.
 */