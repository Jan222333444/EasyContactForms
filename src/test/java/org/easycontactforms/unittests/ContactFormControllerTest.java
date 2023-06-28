package org.easycontactforms.unittests;

import org.easycontactforms.core.controller.ContactFormController;
import org.easycontactforms.core.dtos.ContactFormDto;
import org.easycontactforms.core.dtos.ErrorDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.services.ContactFormService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ContactFormControllerTest {

    @Test
    public void testHandlerFullCorrect(){
        ContactFormDto dto = new ContactFormDto("test@email.com", "nameString", "subjectString", "message");

        ContactFormService service = Mockito.mock(ContactFormService.class);
        ContactForm contactForm = ContactForm.fromContactFormDto(dto);
        Mockito.when(service.saveContactForm(dto)).thenReturn(contactForm);

        ContactFormController controller = new ContactFormController(service);

        ResponseEntity response = controller.handleRequest(dto);

        Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Assertions.assertEquals(contactForm, response.getBody());

    }

    @Test
    public void testHandlerMinimalCorrect(){
        ContactFormDto dto = new ContactFormDto("test@email.com", null, null, "message");

        ContactFormService service = Mockito.mock(ContactFormService.class);
        ContactForm contactForm = ContactForm.fromContactFormDto(dto);
        Mockito.when(service.saveContactForm(dto)).thenReturn(contactForm);

        ContactFormController controller = new ContactFormController(service);

        ResponseEntity response = controller.handleRequest(dto);

        Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Assertions.assertEquals(contactForm, response.getBody());

    }

    @Test
    public void testHandlerMissingEmail(){
        ContactFormDto dto = new ContactFormDto(null, "nameString", "subjectString", "message");
        ContactFormService service = Mockito.mock(ContactFormService.class);

        ContactFormController controller = new ContactFormController(service);

        ErrorDto expectedError = new ErrorDto(400, "Missing required attributes in request body");

        ResponseEntity response = controller.handleRequest(dto);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedError, response.getBody());
    }
}
