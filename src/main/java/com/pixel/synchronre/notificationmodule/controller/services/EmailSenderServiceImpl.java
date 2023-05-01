package com.pixel.synchronre.notificationmodule.controller.services;

import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService
{
    private final JavaMailSender javaMailSender;
    private final HTMLEmailBuilder htmlEmailBuilder;
    private final EmailServiceConfig emailServiceConfig;
    @Value("${auth.server.address}")
    private String authServerAddress;
    @Value("${synchronre.server.address}")
    private String synchronreAdress;

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
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.PASSWORD_REINITIALISATION_REQUEST_OBJECT, htmlEmailBuilder.buildPasswordReinitialisationHTMLEmail(recipientUsername, authServerAddress + link));
    }

    @Override
    public void sendAccountActivationEmail(String receiverMail, String recipientUsername, String activationLink) throws IllegalAccessException
    {
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, htmlEmailBuilder.buildAccountActivationHTMLEmail(recipientUsername, authServerAddress + activationLink));
    }

    @Override
    public void sendNoteCessionEmail(String senderMail, String receiverMail, String interlocName, String affCode, Long plaId, String mailObject) throws IllegalAccessException
    {
        String message = this.htmlEmailBuilder.buildNoteCessionEmail(interlocName, affCode, synchronreAdress + "/reports/note-cession/" + plaId);
        this.sendEmail( senderMail,  receiverMail,  mailObject,  message);
    }
}
