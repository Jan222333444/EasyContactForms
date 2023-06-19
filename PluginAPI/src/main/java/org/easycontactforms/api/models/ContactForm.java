package org.easycontactforms.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {

    private int id;

    private String email;

    private String name;

    private String subject;

    private String message;


    public ContactForm(String email, String name, String subject, String message){
        this.email = email;
        this.name = name;
        this.subject = subject;
        this.message = message;
    }
    public static ContactForm fromContactFormDto(ContactFormDto contactFormDto){
        return new ContactForm(contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getSubject(), contactFormDto.getMessage());
    }
}
