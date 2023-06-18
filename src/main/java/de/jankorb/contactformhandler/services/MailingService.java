package de.jankorb.contactformhandler.services;

import de.jankorb.contactformhandler.models.ContactForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Slf4j
@Service
public class MailingService {

    @Value("${mail.user.name}")
    private String name;

    @Value("${mail.user.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.ssl.enable}")
    private String enable;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.ssl.protocols}")
    private String protocols;

    @Value("${mail.smtp.ssl.trust}")
    private String trust;

    @Value("${mail.user.address}")
    private String address;

    @Value("${mail.recipient.address}")
    private String recipient;

    @Value("${mail.user.displayName}")
    private String displayName;

    public void sendMail(ContactForm contactForm) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", auth);
        prop.put("mail.smtp.ssl.enable", enable);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.protocols", protocols);
        prop.put("mail.smtp.ssl.trust", trust);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        name, password);
            }
        });
        String msg = renderHTML(contactForm);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(address, displayName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("New Contact Request from: " + contactForm.getEmail());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public String renderHTML(ContactForm contactForm) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        String[] msg = contactForm.getMessage().split("\n");


        Context context = new Context();
        context.setVariable("name", contactForm.getName() == null ? "" : contactForm.getName());
        context.setVariable("subject", contactForm.getSubject() == null ? "" : contactForm.getSubject());
        context.setVariable("message", msg);
        context.setVariable("email", contactForm.getEmail());
        context.setVariable("id", contactForm.getId());

        return templateEngine.process("mail-template", context);
    }
}
