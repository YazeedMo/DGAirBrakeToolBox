package com.dgairbraketoolbox.dgairbraketoolbox.services.emailservice;

import com.dgairbraketoolbox.dgairbraketoolbox.models.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface EmailHandler {

    void createDraft(Email email) throws MessagingException, IOException, GeneralSecurityException;

    void sendMessage(Email email) throws IOException, GeneralSecurityException, MessagingException;


    }
