package com.pixel.synchronre.notificationmodule.controller.services;

public interface HTMLEmailBuilder
{
    String buildAccountActivationHTMLEmail(String recipientUsername, String link);
    String buildPasswordReinitialisationHTMLEmail(String recipientUsername, String link);
    String buildNoteCessionFacEmail(String recipientUsername, String affCode);
    String buildNoteDebitFacEmail(String recipientUsername, String affCode);
    //String buildNoteCreditFacEmail(String recipientUsername, String affCode);

    //String synchronreEmail, String cesEmail, String cesInterlocuteur, String affCode, Long sinId, Long cesId, String noteCession
    //String buildNoteCessionSinistreEmail(String cesInterlocuteur, String affCode, Long sinId, Long cesId);
    //String buildNoteDebitSinistreEmail(String cesInterlocuteur, String affCode, Long sinId, Long cesId);
    String buildNoteDeDebitSinistreEmail(String cesInterlocuteur, String sinCode);
    String buildNoteCessionSinistreEmail(String cesInterlocuteur, String sinCode);

}
