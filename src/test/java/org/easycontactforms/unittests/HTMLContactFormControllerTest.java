package org.easycontactforms.unittests;

import org.easycontactforms.core.controller.HTMLContactFormController;
import org.easycontactforms.core.dtos.HTMLContactFormDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.services.ContactFormService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HTMLContactFormControllerTest {

    @Test
    public void testHTMLContactFormController(){
        HTMLContactFormDto dto = new HTMLContactFormDto("/success", "/error");
        dto.setName("");
        dto.setEmail("");
        dto.setMessage("");
        dto.setSubject(null);
        ContactFormService serviceMock = Mockito.mock(ContactFormService.class);
        Mockito.when(serviceMock.saveContactForm(dto)).thenReturn(new ContactForm());
        HTMLContactFormController controller = new HTMLContactFormController(serviceMock);
        String result = controller.processPlainHTMLForm("localhost", dto);
        Assertions.assertEquals("redirect:localhost/success", result);
    }

}
