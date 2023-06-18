package de.jankorb.contactformhandler.unittests;

import de.jankorb.contactformhandler.models.ContactForm;
import de.jankorb.contactformhandler.services.MailingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MailingServiceTest {

    @Test
    public void testRender(){
        MailingService mailingService = new MailingService();

        ContactForm contactForm = new ContactForm(1, "test@example.test", "TestName", "","");
        String output = mailingService.renderHTML(contactForm);

        Assertions.assertFalse(output.contains("${"));
        Assertions.assertTrue(output.contains("test@example.test"));
        Assertions.assertTrue(output.contains("TestName"));
    }
}
