
package org.example.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataSource;
import javax.mail.internet.MimeMultipart;
public class EmailSender {
    public static void sendEmail(String to, String subject, String body) {
        final String username = "oussemaa782@gmail.com";
        final String password = "Azerty011201**||";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.office365.com");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            System.out.println("test");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public static void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        final String username = "oussemaa782@gmail.com";
        final String password = "Azerty011201**||";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Create a multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Create the text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Add text part to multipart
            multipart.addBodyPart(textPart);

            // Add attachment if provided
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(new File(attachmentPath).getName());
                attachmentPart.attachFile(new File(attachmentPath));

                multipart.addBodyPart(attachmentPart);

                try (BufferedReader br = new BufferedReader(new FileReader(attachmentPath))) {
                    String line;
                    System.out.println("Content of the file:");
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Set the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        EmailSender.sendEmail("yasmin.jenhani@gmail.com","abc","abc");
    } }