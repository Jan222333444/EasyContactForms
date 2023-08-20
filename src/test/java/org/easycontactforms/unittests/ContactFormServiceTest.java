package org.easycontactforms.unittests;

import org.easycontactforms.core.dtos.ContactFormDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.repositories.ContactFormRepository;
import org.easycontactforms.core.services.ContactFormService;
import org.easycontactforms.core.services.MailingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ContactFormServiceTest {

    @Test
    public void testSaveCorrect(){
        ContactFormDto dto = new ContactFormDto("test@test.com", "testname", "test subject", "test message");
        ContactFormRepository repository = Mockito.mock(ContactFormRepository.class);
        Mockito.when(repository.save(ContactForm.fromContactFormDto(dto))).thenReturn(ContactForm.fromContactFormDto(dto));
        MailingService mailingService = Mockito.mock(MailingService.class);
        ContactFormService service = new ContactFormService(repository, mailingService);

        ContactForm contactForm = service.saveContactForm(dto);

        Assertions.assertEquals(dto.getName(), contactForm.getName());
        Assertions.assertEquals(dto.getSubject(), contactForm.getSubject());
        Assertions.assertEquals(dto.getEmail(), contactForm.getEmail());
        Assertions.assertEquals(dto.getMessage(), contactForm.getMessage());
    }

    @Test
    public void testUpdate(){
        ContactFormDto dto = new ContactFormDto(".com", "testname", "test subject", "test message");
        ContactForm input = ContactForm.fromContactFormDto(dto);
        input.setId(4);
        ContactFormRepository repository = Mockito.mock(ContactFormRepository.class);
        Mockito.when(repository.save(ContactForm.fromContactFormDto(dto))).thenReturn(ContactForm.fromContactFormDto(dto));
        MailingService mailingService = Mockito.mock(MailingService.class);
        ContactFormService service = new ContactFormService(repository, mailingService);

        service.updateContactForm(input);

    }
}
