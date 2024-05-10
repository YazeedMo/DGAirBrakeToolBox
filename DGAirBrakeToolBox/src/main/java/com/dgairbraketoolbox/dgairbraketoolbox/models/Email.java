package com.dgairbraketoolbox.dgairbraketoolbox.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Email {

    private String fromEmailAddress;
    private String toEmailAddress;

    private String subject;

    private String message;

    private List<File> attachments = new ArrayList<>();

    public Email() {

    }

    public Email(String fromEmailAddress, String toEmailAddress, String subject, String message, List<File> attachments) {
        this.fromEmailAddress = fromEmailAddress;
        this.toEmailAddress = toEmailAddress;
        this.subject = subject;
        this.message = message;
        this.attachments = attachments;
    }

    public Email(String fromEmailAddress, String toEmailAddress, String subject, String message) {
        this.fromEmailAddress = fromEmailAddress;
        this.toEmailAddress = toEmailAddress;
        this.subject = subject;
        this.message = message;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(File file) {
        this.attachments.add(file);
    }

}