package org.easycontactforms.core.services;

import org.easycontactforms.core.dtos.ContactFormDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.repositories.ContactFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ContactFormService {

    private final ArrayList<ContactForm> contactForms = new ArrayList<>();
    private final ContactFormRepository repository;

    private final MailingService mailingService;

    @Value("${redirect.mode}")
    private String mode;

    @Autowired
    public ContactFormService(ContactFormRepository repository, MailingService mailingService){
        this.repository = repository;
        this.mailingService = mailingService;
    }
    public ContactForm saveContactForm(ContactFormDto contactFormDto){
        contactForms.add(ContactForm.fromContactFormDto(contactFormDto));
        ContactForm contactForm = repository.save(ContactForm.fromContactFormDto(contactFormDto));
        if(mode.equalsIgnoreCase("email")){
            log.info("Redirecting Contact Form to email");
            MailSendThread thread = new MailSendThread(this, mailingService, contactForm);
            thread.start();
        }
        log.info("Saved Contact Form");
        return contactForm;
    }

    public ContactForm updateContactForm(ContactForm contactForm){
        repository.save(contactForm);
        return contactForm;
    }

    public List<ContactForm> getContactForms(boolean onlyNotSendEmails){

        if(!onlyNotSendEmails){
            return repository.findAll();
        }else {
            return repository.findByEmailSent(false);
        }
    }
}
