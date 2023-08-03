package com.pixel.synchronre.notificationmodule.controller.services;

import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import com.pixel.synchronre.notificationmodule.model.dto.EmailAttachment;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService
{
    private final JavaMailSender javaMailSender;
    private final HTMLEmailBuilder htmlEmailBuilder;
    private final EmailServiceConfig emailServiceConfig;
    private final IServiceReport reportService;
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final SinRepo sinRepo;
    @Value("${auth.server.address}")
    private String authServerAddress;
    @Value("${synchronre.server.address}")
    private String synchronreAdress;
    @Value("${front.adress}")
    private String frontAddress;

    @Override @Async
    public void sendEmailWithAttachments(String senderMail, String receiverMail, String mailObject, String message, List<EmailAttachment> attachments) throws IllegalAccessException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setText(message, true); // Second parameter true means that the message will be an HTML message
            mimeMessageHelper.setTo(receiverMail);
            mimeMessage.setSubject(mailObject);
            mimeMessage.setFrom(senderMail);

            // Add attachments to the email
            if (attachments != null && !attachments.isEmpty()) {
                for (EmailAttachment attachment : attachments) {
                    DataSource dataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getContentType());
                    mimeMessageHelper.addAttachment(attachment.getFilename(), dataSource);
                }
            }

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalAccessException("Error while sending email");
        }
    }


    @Override
    @Async
    public void sendEmail(String senderMail, String receiverMail, String mailObject, String message) throws IllegalAccessException {
        try
        {
            MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(message, true); // Second parameter true means that the message will be an HTML message
            mimeMessageHelper.setTo(receiverMail);
            mimeMessage.setSubject(mailObject);
            mimeMessage.setFrom(senderMail);
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            throw new IllegalAccessException("Error while sending email");
        }
    }

    @Override
    public void sendReinitialisePasswordEmail(String receiverMail, String recipientUsername, String link) throws IllegalAccessException
    {
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.PASSWORD_REINITIALISATION_REQUEST_OBJECT, htmlEmailBuilder.buildPasswordReinitialisationHTMLEmail(recipientUsername, frontAddress + link));
    }

    @Override
    public void sendAccountActivationEmail(String receiverMail, String recipientUsername, String activationLink) throws IllegalAccessException
    {
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, htmlEmailBuilder.buildAccountActivationHTMLEmail(recipientUsername, frontAddress + activationLink));
    }

    @Override
    public void sendNoteCessionFacEmail(String senderMail, String receiverMail, String interlocName, String affCode, Long plaId, String mailObject) throws Exception
    {
        String message = this.htmlEmailBuilder.buildNoteCessionFacEmail(interlocName, affCode);
        byte[] report = reportService.generateNoteCessionFac(plaId);
        EmailAttachment attachment = new EmailAttachment("Note_de_cession_facultative.pdf", report, "application/pdf");
        this.sendEmailWithAttachments( senderMail,  receiverMail,  mailObject,  message, Collections.singletonList(attachment));
    }
    @Override
    public void sendNoteDebitFacEmail(String senderMail, String receiverMail, String interlocName, Long affId) throws Exception {
        String affCode = affRepo.getAffCode(affId);
        String message = this.htmlEmailBuilder.buildNoteDebitFacEmail(receiverMail, affCode);
        byte[] report = reportService.generateNoteDebitFac(affId);
        EmailAttachment attachment = new EmailAttachment("Note_de_débit_facultative.pdf", report, "application/pdf");
        this.sendEmailWithAttachments( senderMail,  receiverMail,  "Note de débit facultative de l'affaire N° " + affCode,  message, Collections.singletonList(attachment));
    }

    @Override
    public void sendNoteCreditFacEmail(String senderMail, String receiverMail, String interlocName, Long affId, Long cesId) throws Exception {
        String affCode = affRepo.getAffCode(affId);
        String message = this.htmlEmailBuilder.buildNoteDebitFacEmail(receiverMail, affCode);
        byte[] report = reportService.generateNoteCreditFac(affId, cesId);
        EmailAttachment attachment = new EmailAttachment("Note de crédit facultative", report, "application/pdf");
        this.sendEmailWithAttachments( senderMail,  receiverMail,  "Note de crédit facultative de l'affaire N° " + affCode,  message, Collections.singletonList(attachment));
    }

    @Override
    public void sendNoteCessionSinistreEmail(String synchronreEmail, String cesEmail, String cesInterlocuteur, String affCode, Long sinId, Long cesId, String noteCession) throws Exception {
        String message = this.htmlEmailBuilder.buildNoteCessionSinistreEmail(cesInterlocuteur, sinRepo.getSinCode(sinId));
        byte[] report = reportService.generateNoteCessionSinistre(sinId, cesId);
        EmailAttachment attachment = new EmailAttachment("Note de cession sinistre", report, "application/pdf");
        this.sendEmailWithAttachments( synchronreEmail,  cesEmail,  "Note de cession et de débit sinistre",  message, Collections.singletonList(attachment));
    }




    @Override
    public void sendNoteDebitSinistreEmail(String senderMail, String receiverMail, String interlocName, Long sinId) throws Exception
    {
        String message = this.htmlEmailBuilder.buildNoteDeDebitSinistreEmail(interlocName, sinRepo.getSinCode(sinId));
        byte[] report = reportService.generateNoteDebitSinistre(sinId);
        EmailAttachment attachment = new EmailAttachment("Note de cession sinistre", report, "application/pdf");
        this.sendEmailWithAttachments( senderMail,  receiverMail,  "Note de cession et de débit sinistre",  message, Collections.singletonList(attachment));
    }



    @Override
    public void sendCheque(String senderMail, String receiverMail, String interlocName, Long regId) throws Exception {

    }
}
