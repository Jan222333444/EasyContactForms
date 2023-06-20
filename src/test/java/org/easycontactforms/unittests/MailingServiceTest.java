package org.easycontactforms.unittests;

import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.services.ContactFormService;
import org.easycontactforms.core.services.MailingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class MailingServiceTest {

    @Test
    public void testRender(){
        MailingService mailingService = new MailingService();

        ContactForm contactForm = new ContactForm("test@example.test", "TestName", "", "");
        contactForm.setId(1);
        String output = mailingService.renderHTML(contactForm);

        Assertions.assertFalse(output.contains("${"));
        Assertions.assertTrue(output.contains("test@example.test"));
        Assertions.assertTrue(output.contains("TestName"));
    }
}
