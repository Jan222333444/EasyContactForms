package org.easycontactforms.core.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.core.ApplicationState;
import org.easycontactforms.core.services.ContactFormService;
import org.easycontactforms.core.services.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResendEmailsScheduler {

    private final MailingService mailingService;

    private final ContactFormService contactFormService;


    @Autowired
    public ResendEmailsScheduler(MailingService mailingService, ContactFormService contactFormService){
        this.mailingService = mailingService;
        this.contactFormService = contactFormService;
    }

    @Scheduled(fixedRateString = "${redirect.mode.resend.interval}")
    public void resendEmails(){
        log.info("Resending not send emails");
        mailingService.resendMails(true, contactFormService);
    }
}
