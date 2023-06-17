package de.jankorb.contactformhandler.controller;

import de.jankorb.contactformhandler.dtos.ContactFormDto;
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


    private ContactFormService service;


    @Autowired
    public ContactFormController(ContactFormService service){
        this.service = service;
    }

    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactFormDto> processContactForm(@RequestBody @Valid ContactFormDto contactFormDto){
        log.debug("[POST] Saving Contact Form");
        service.saveContactForm(contactFormDto);
        return ResponseEntity.ok(contactFormDto);
    }
}
