package de.jankorb.contactformhandler.models;

import de.jankorb.contactformhandler.dtos.ContactFormDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {
    private int id;

    @NotNull
    @Email
    private String email;

    private String name;

    @NotNull
    private String message;


    public static ContactForm fromContactFormDto(ContactFormDto contactFormDto){
        return new ContactForm(0, contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getMessage());
    }
}
