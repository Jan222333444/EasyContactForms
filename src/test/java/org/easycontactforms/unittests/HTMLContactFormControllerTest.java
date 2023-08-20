package org.easycontactforms.unittests;

import org.easycontactforms.api.Plugin;
import org.easycontactforms.core.controller.HTMLContactFormController;
import org.easycontactforms.core.dtos.HTMLContactFormDto;
import org.easycontactforms.core.models.ContactForm;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.easycontactforms.core.services.ContactFormService;
import org.easycontactforms.unittests.testpluginclasses.Testplugin1;
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

    @Test
    public void testHTMLContactFormControllerPlugin(){
        Plugin plugin = new Testplugin1();
        PluginStore.instance.plugins.put("first", plugin);
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

    @Test
    public void testHTMLContactFormControllerOriginInRedirect(){
        HTMLContactFormDto dto = new HTMLContactFormDto("localhost/success", "localhost/error");
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

    @Test
    public void testHTMLContactFormControllerNoOrigin(){
        HTMLContactFormDto dto = new HTMLContactFormDto("/success", "localhost/error");
        dto.setName("");
        dto.setEmail("");
        dto.setMessage("");
        dto.setSubject(null);
        ContactFormService serviceMock = Mockito.mock(ContactFormService.class);
        Mockito.when(serviceMock.saveContactForm(dto)).thenReturn(new ContactForm());
        HTMLContactFormController controller = new HTMLContactFormController(serviceMock);
        String result = controller.processPlainHTMLForm(null, dto);
        Assertions.assertEquals("redirect:/success", result);
    }

    @Test
    public void testHTMLContactFormControllerNoRedirect(){
        HTMLContactFormDto dto = new HTMLContactFormDto(null, null);
        dto.setName("");
        dto.setEmail("");
        dto.setMessage("");
        dto.setSubject(null);
        ContactFormService serviceMock = Mockito.mock(ContactFormService.class);
        Mockito.when(serviceMock.saveContactForm(dto)).thenReturn(new ContactForm());
        HTMLContactFormController controller = new HTMLContactFormController(serviceMock);
        String result = controller.processPlainHTMLForm("localhost", dto);
        Assertions.assertEquals("redirect:localhost", result);
    }

    @Test
    public void testHTMLError(){
        HTMLContactFormDto dto = new HTMLContactFormDto("/sucess", "/error");
        ContactFormService serviceMock = Mockito.mock(ContactFormService.class);
        Mockito.when(serviceMock.saveContactForm(dto)).thenReturn(new ContactForm());
        HTMLContactFormController controller = new HTMLContactFormController(serviceMock);
        String result = controller.processPlainHTMLForm("localhost", dto);
        Assertions.assertEquals("redirect:localhost/error", result);
    }

    @Test
    public void testHTMLErrorOriginInRedirect(){
        HTMLContactFormDto dto = new HTMLContactFormDto("localhost/sucess", "localhost/error");
        ContactFormService serviceMock = Mockito.mock(ContactFormService.class);
        Mockito.when(serviceMock.saveContactForm(dto)).thenReturn(new ContactForm());
        HTMLContactFormController controller = new HTMLContactFormController(serviceMock);
        String result = controller.processPlainHTMLForm("localhost", dto);
        Assertions.assertEquals("redirect:localhost/error", result);
    }

}
