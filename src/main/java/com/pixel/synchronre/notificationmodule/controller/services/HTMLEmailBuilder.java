package com.pixel.synchronre.notificationmodule.controller.services;

public interface HTMLEmailBuilder
{
    String buildAccountActivationHTMLEmail(String recipientUsername, String link);
    String buildPasswordReinitialisationHTMLEmail(String recipientUsername, String link);
    String buildNoteCessionEmail(String recipientUsername, String affCode, String link);

    String buildNoteCessionSinistreEtNoteDebitEmail(String cesEmail, String cesInterlocuteur, String affCode, String s, String s1);
}
