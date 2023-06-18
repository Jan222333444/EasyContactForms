package de.jankorb.contactformhandler.controller;

import de.jankorb.contactformhandler.PluginStore;
import de.jankorb.contactformhandler.dtos.ContactFormDto;
import de.jankorb.contactformhandler.dtos.ErrorDto;
import de.jankorb.contactformhandler.models.ContactForm;
import de.jankorb.contactformhandler.services.ContactFormService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/contact")
public class ContactFormController {


    private final ContactFormService service;


    @Autowired
    public ContactFormController(ContactFormService service){
        this.service = service;
    }

    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processContactForm(@RequestBody @Valid ContactFormDto contactFormDto){
        log.debug("[POST] Saving Contact Form");
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).beforeContactFormProcessing(new de.jankorb.contactformhandler.api.models.ContactFormDto(contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
        }
        if (contactFormDto.getEmail() == null || contactFormDto.getMessage() == null){
            return ResponseEntity.status(400).body(new ErrorDto(400, "Missing required attributes in request body"));
        }
        ContactForm contactForm = service.saveContactForm(contactFormDto);
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).contactFormProcessed(new de.jankorb.contactformhandler.api.models.ContactForm(contactForm.getId(), contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
        }
        log.debug("[POST] successfully processed contact form");
        return ResponseEntity.ok(contactFormDto);
    }
}
