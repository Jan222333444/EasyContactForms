package org.easycontactforms.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.easycontactforms.core.dtos.ContactFormDto;
import org.easycontactforms.core.dtos.ErrorDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.services.ContactFormService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for handling contact requests with JSON or XML body
 */
@Tag(name="contact-submission", description = "All contact submission related endpoints")
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
     * receives request if body contains JSON
     * @param contactFormDto request body
     * @return status and in db saved object
     */
    @Operation(summary = "Add contact request",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "authorization",
                            description = "Optional Authorization via Bearer token",
                            required = false,
                            schema = @Schema(type = "string"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new entry",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactForm.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @CrossOrigin
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity processContactForm(@RequestBody @Valid ContactFormDto contactFormDto){
        return handleRequest(contactFormDto);
    }

    /**
     * receives request if body contains XML
     * @param contactFormDto request body
     * @return status and in db saved object
     */
    @CrossOrigin
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE}, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity processContactFormXML(@RequestBody @Valid ContactFormDto contactFormDto){
        log.error("Email " + contactFormDto.getEmail());
        return handleRequest(contactFormDto);
    }

    /**
     * handles controller logic and handles plugin hooks
     * @param contactFormDto request body
     * @return status and in db saved object
     */
    public ResponseEntity handleRequest(ContactFormDto contactFormDto){
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
