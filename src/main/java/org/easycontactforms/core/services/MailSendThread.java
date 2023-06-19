package org.easycontactforms.core.services;

import org.easycontactforms.core.PluginStore;
import org.easycontactforms.core.models.ContactForm;

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
            PluginStore.instance.plugins.get(key).onMailSent(new org.easycontactforms.api.models.ContactForm(contactForm.getId(), contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(), contactForm.getMessage()));
        }
    }
}
