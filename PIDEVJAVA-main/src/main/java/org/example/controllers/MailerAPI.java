package org.example.controllers;

import java.util.Date;
import java.util.Properties;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message;

public class MailerAPI {

    static Session session;
    static Properties properties = new Properties();

    public static void sendMail(String username, String password, String recipient, String subject, String content) {
        try {
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.office365.com"); // Outlook SMTP server
            properties.put("mail.smtp.port", "587"); // TLS Port for Outlook

            session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(content, "text/plain");

            Transport.send(message);

            System.out.println("Email successfully sent to: " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("An error occurred while sending the email.");
        }
    }
}
