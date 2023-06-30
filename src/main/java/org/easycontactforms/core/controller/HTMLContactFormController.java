package org.easycontactforms.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.core.dtos.HTMLContactFormDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.easycontactforms.core.services.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling form-data requests (plain HTML Form post)
 */
@Controller
@Slf4j
@RequestMapping("/contact")
public class HTMLContactFormController {

    private final ContactFormService service;

    @Autowired
    public HTMLContactFormController(ContactFormService service) {
        this.service = service;
    }

    /**
     * Handles plain form posts without javascript used
     *
     * @param origin         Server of website
     * @param contactFormDto Form data in plain html post encoding
     * @return proper redirect back to website if possible
     */
    @CrossOrigin
    @PostMapping
    public String processPlainHTMLForm(@RequestHeader(HttpHeaders.ORIGIN) String origin, HTMLContactFormDto contactFormDto) {
        try {
            log.debug("[POST] Saving Contact Form");

            // Processing plugin hook before contact form processing
            for (String key : PluginStore.instance.plugins.keySet()) {
                PluginStore.instance.plugins.get(key).beforeContactFormProcessing(new org.easycontactforms.api.models.ContactFormDto(contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
            }
            if (contactFormDto.getEmail() == null || contactFormDto.getMessage() == null) {
                throw new RuntimeException();
            }
            // processes contact form
            ContactForm contactForm = service.saveContactForm(contactFormDto);
            log.debug("[POST] processed data excluding plugins");

            // Executing plugin hook contactFormProcessed
            for (String key : PluginStore.instance.plugins.keySet()) {
                PluginStore.instance.plugins.get(key).contactFormProcessed(new org.easycontactforms.api.models.ContactForm(contactForm.getId(), contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage()));
            }
            log.debug("[POST] successfully processed contact form");

            /*
             * Redirect handling
             */
            return getRedirect(origin, contactFormDto);

        } catch (Exception e) {

            if (contactFormDto.getRedirectError() != null) {
                assert origin != null;
                if (contactFormDto.getRedirectError().contains(origin)) {
                    return "redirect:" + contactFormDto.getRedirectError();
                }
                return "redirect:" + origin + contactFormDto.getRedirectError();
            }

        }
        return "redirect:" + origin;
    }

    /**
     * Redirect success handling
     */
    private String getRedirect(String origin, HTMLContactFormDto contactFormDto){
        if (contactFormDto.getRedirect() != null) {
            if (origin == null) {
                return "redirect:" + contactFormDto.getRedirect();
            }
            if (contactFormDto.getRedirect().contains(origin)) {
                return "redirect:" + contactFormDto.getRedirect();
            }
            return "redirect:" + origin + contactFormDto.getRedirect();
        }
        log.info("[POST][FORM-DATA] Received invalid redirect input");
        throw new RuntimeException();
    }
}
