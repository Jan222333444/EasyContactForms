package de.jankorb.contactformhandler.services;

import de.jankorb.contactformhandler.dtos.ContactFormDto;
import de.jankorb.contactformhandler.models.ContactForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Slf4j
@Service
public class ContactFormService {

    private ArrayList<ContactForm> contactForms = new ArrayList<>();
    public void saveContactForm(ContactFormDto contactFormDto){
        contactForms.add(ContactForm.fromContactFormDto(contactFormDto));
        log.info("Saved Contact Form");
    }
}
