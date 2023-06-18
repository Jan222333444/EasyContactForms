package de.jankorb.contactformhandler.services;

import de.jankorb.contactformhandler.dtos.ContactFormDto;
import de.jankorb.contactformhandler.models.ContactForm;
import de.jankorb.contactformhandler.repositories.ContactFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Slf4j
@Service
public class ContactFormService {

    private ArrayList<ContactForm> contactForms = new ArrayList<>();
    private ContactFormRepository repository;

    private MailingService mailingService;

    @Value("${redirect.mode}")
    private String mode;

    @Autowired
    public ContactFormService(ContactFormRepository repository, MailingService mailingService){
        this.repository = repository;
        this.mailingService = mailingService;
    }
    public void saveContactForm(ContactFormDto contactFormDto){
        contactForms.add(ContactForm.fromContactFormDto(contactFormDto));
        ContactForm contactForm = repository.save(ContactForm.fromContactFormDto(contactFormDto));
        if(mode.equalsIgnoreCase("email")){
            log.info("Redirecting Contact Form to email");
            mailingService.sendMail(contactForm);
        }
        log.info("Saved Contact Form");
    }
}
