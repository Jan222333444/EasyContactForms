package de.jankorb.contactformhandler.services;

import de.jankorb.contactformhandler.PluginStore;
import de.jankorb.contactformhandler.models.ContactForm;

public class MailSendThread extends Thread{

    private final MailingService mailingService;
    private final ContactForm contactForm;
    public MailSendThread(MailingService mailingService, ContactForm contactForm){
        this.mailingService = mailingService;
        this.contactForm = contactForm;
    }
    @Override
    public void run() {
        super.run();
        mailingService.sendMail(contactForm);
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).onMailSent(new de.jankorb.contactformhandler.api.models.ContactForm(contactForm.getId(), contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(), contactForm.getMessage()));
        }
    }
}
