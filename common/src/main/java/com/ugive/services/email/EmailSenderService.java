package com.ugive.services.email;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}
