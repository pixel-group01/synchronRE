package com.pixel.synchronre.notificationmodule.controller.services;


import com.pixel.synchronre.notificationmodule.model.dto.EmailAttachment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

public interface EmailSenderService
{
    @Async
    void sendEmailWithAttachments(String senderMail, String receiverMail, String mailObject, String message, List<EmailAttachment> attachments) throws IllegalAccessException;

    void sendEmail(String senderMail, String receiverMail, String mailObject, String message) throws IllegalAccessException;
    void sendReinitialisePasswordEmail(String receiverMail, String recipientUsername, String link) throws IllegalAccessException;
    void sendAccountActivationEmail(String receiverMail, String recipientUsername, String activationLink) throws IllegalAccessException;

    void sendNoteCessionFacEmail(String senderMail, String receiverMail, String interlocName, String affCode, Long plaId, String mailObject) throws Exception;
    void sendNoteDebitFacEmail(String senderMail, String receiverMail, String interlocName,Long affId) throws Exception;
    void sendNoteCreditFacEmail(String senderMail, String receiverMail, String interlocName,Long affId, Long cesId) throws Exception;

    void sendNoteCessionSinistreEmail(String synchronreEmail, String affCode, Long sinId, Long cesId, String note_de_cession_sinistre) throws Exception;
    void sendNoteDebitSinistreEmail(String senderMail, String receiverMail, String interlocName,Long affId) throws Exception;

    void sendCheque(String senderMail, String receiverMail, String interlocName,Long regId) throws Exception;


    void envoyerEmailTransmissionAffaireAuSouscripteur( String receiverMail, String nomDestinataire, String nomCedante);
    void envoyerEmailRetourAffaireALaCedante( String receiverMail, String nomDestinataire, String affCode, String motif) ;
    void envoyerEmailTransmissionAffaireALaCompta( String receiverMail, String nomDestinataire);
    //Sinistre
    void envoyerEmailTransmissionSinistreAuSouscripteur( String receiverMail, String nomDestinataire, String nomCedante);
    void envoyerEmailRetourSinistreALaCedante( String receiverMail, String nomDestinataire, String sinCode, String motif) ;
    void envoyerEmailSinistreEnAttenteDeValidationAuValidateur( String receiverMail, String nomDestinataire);
    void envoyerEmailRetourSinistreAuSouscripteur( String receiverMail, String nomDestinataire, String sinCode, String motif) ;
    void envoyerEmailSinistreEnAttenteDePaiementAuComptable( String receiverMail, String nomDestinataire);
    void envoyerEmailRetourSinistreAuValidateur( String receiverMail, String nomDestinataire, String sinCode, String motif) ;

    void envoyerMailPourPlacementEnAttenteDeValidation(String receiverMail, String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise);

    void envoyerMailPourPlacementRetourne(String email, String firstName, String affCode, String cesNom, BigDecimal repCapital, String devise, String motif);

    void envoyerMailPourEnvoieDeNoteDeCession(String email, String firstName, String affCode, String cesNom, BigDecimal repCapital, String s);


    //void sendConfirmationEmail(String receiverMail, String link) throws IllegalAccessException;

    //void resendConfirmationEmail(String receiverMail, String oldActivationToken, String link) throws IllegalAccessException;

}
