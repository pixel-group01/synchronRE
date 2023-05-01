package com.pixel.synchronre.notificationmodule.controller.services;

public interface HTMLEmailBuilder
{
    String buildAccountActivationHTMLEmail(String recipientUsername, String link);
    String buildPasswordReinitialisationHTMLEmail(String recipientUsername, String link);
    String buildNoteCessionEmail(String recipientUsername, String affCode, String link);
}
