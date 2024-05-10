package com.dgairbraketoolbox.dgairbraketoolbox.services.emailservice;

import com.dgairbraketoolbox.dgairbraketoolbox.models.Email;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GMailer implements EmailHandler {

    // Application name
    private static final String APPLICATION_NAME = "DG Air Brake Tool Box";

    // Global instance of the JSON factory
    private static final JsonFactory JSON_FACTORY= GsonFactory.getDefaultInstance();

    // Directory to store authorization tokens for this application
    private static final String TOKENS_DIRECTORY_PATH = "token";

    // Global instance of the scopes required for this application
    // If modifying these scopes, delete previously saved tokens/folder
    private static final List<String> SCOPES = new ArrayList<>(Arrays.asList(
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_SEND
    ));

    private static final String CREDENTIALS_FILE_PATH = "/gmailer/credentials.json";

    // Creates an authorized Credential object.
    // HTTP_TRANSPORT -> The network HTTP Transport.
    // returns -> An authorized Credential object.
    // throws IOException -> If the credentials.json file cannot be found.
    private static Credential getCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        // Load client secrets
        InputStream in = GMailer.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resources not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return credential;

    }

    // Convert Email to MimeMessage
    private static MimeMessage createMimeMessage(Email email) throws MessagingException {

        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        // Add fromEmailAddress, toEmailAddress, nd subject to email
        mimeMessage.setFrom(new InternetAddress(email.getFromEmailAddress()));
        mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email.getToEmailAddress()));
        mimeMessage.setSubject(email.getSubject());

        // Add body content to message
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(email.getMessage(), "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        // Add all attachments, if any
        if (!email.getAttachments().isEmpty()) {

            for (File file : email.getAttachments()) {
                mimeBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                mimeBodyPart.setDataHandler(new DataHandler(source));
                mimeBodyPart.setFileName(file.getName());

                multipart.addBodyPart(mimeBodyPart);
            }

            mimeMessage.setContent(multipart);

            return mimeMessage;
        }

        mimeMessage.setContent(multipart);

        return mimeMessage;

    }

    // Convert MimeMessage to Message
    private static Message encodeMimeMessage(MimeMessage mimeMessage) throws MessagingException, IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedMimeMessage = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();
        message.setRaw(encodedMimeMessage);

        return message;
    }

    // Send Message
    @Override
    public void sendMessage(Email email) throws IOException, GeneralSecurityException, MessagingException {

        MimeMessage mimeMessage = createMimeMessage(email);

        Message message = encodeMimeMessage(mimeMessage);

        final  NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        service.users().messages().send("me", message).execute();

    }

    // Save message as Draft
    @Override
    public void createDraft(Email email) throws MessagingException, IOException, GeneralSecurityException {

        MimeMessage mimeMessage = createMimeMessage(email);

        Message message = encodeMimeMessage(mimeMessage);

        final  NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Draft draft = new Draft();
        draft.setMessage(message);

        service.users().drafts().create("me", draft).execute();

    }

}
