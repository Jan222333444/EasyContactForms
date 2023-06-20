package org.easycontactforms.core.services;

import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.core.ApplicationState;
import org.easycontactforms.core.PluginStore;
import org.easycontactforms.core.models.ContactForm;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
public class MailSendThread extends Thread {

    private final MailingService mailingService;

    private final ContactFormService contactFormService;
    private final ContactForm contactForm;

    @Value("${redirect.mode.resend}")
    private boolean resendPolicy;

    public MailSendThread(ContactFormService contactFormService, MailingService mailingService, ContactForm contactForm) {
        this.contactFormService = contactFormService;
        this.mailingService = mailingService;
        this.contactForm = contactForm;
    }

    @Override
    public void run() {
        super.run();
        try {
            mailingService.sendMail(contactForm);
        } catch (MessagingException e) {
            log.error("Could not connect to smtp server");
            ApplicationState.instance.smtpAvailable = false;
            return;
        } catch (UnsupportedEncodingException e) {
            ApplicationState.instance.smtpAvailable = false;
            e.printStackTrace();
            return;
        }
        if(!ApplicationState.instance.smtpAvailable && resendPolicy){
            ApplicationState.instance.smtpAvailable = true;
            mailingService.resendMails(true, contactFormService);
        }
        contactForm.setEmailSent(true);
        contactFormService.updateContactForm(contactForm);
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onMailSent(new org.easycontactforms.api.models.ContactForm(contactForm.getId(), contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(), contactForm.getMessage()));
        }
    }
}
