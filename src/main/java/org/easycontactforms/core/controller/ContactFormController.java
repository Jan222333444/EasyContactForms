package org.easycontactforms.core.controller;

import org.easycontactforms.core.PluginStore;
import org.easycontactforms.core.dtos.ContactFormDto;
import org.easycontactforms.core.dtos.ErrorDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.services.ContactFormService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for handling contact requests
 */
@RestController
@Slf4j
@RequestMapping("/contact")
public class ContactFormController {


    private final ContactFormService service;


    @Autowired
    public ContactFormController(ContactFormService service){
        this.service = service;
    }

    /**
     * receives request and handles plugin hooks
     * @param contactFormDto request body
     * @return status and in db saved object
     */
    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processContactForm(@RequestBody @Valid ContactFormDto contactFormDto){
        log.debug("[POST] Saving Contact Form");
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).beforeContactFormProcessing(new org.easycontactforms.api.models.ContactFormDto(contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
        }
        if (contactFormDto.getEmail() == null || contactFormDto.getMessage() == null){
            return ResponseEntity.status(400).body(new ErrorDto(400, "Missing required attributes in request body"));
        }
        ContactForm contactForm = service.saveContactForm(contactFormDto);
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).contactFormProcessed(new org.easycontactforms.api.models.ContactForm(contactForm.getId(), contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
        }
        log.debug("[POST] successfully processed contact form");
        return ResponseEntity.accepted().body(contactForm);
    }
}
