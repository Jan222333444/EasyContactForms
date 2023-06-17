package de.jankorb.contactformhandler.models;

import de.jankorb.contactformhandler.dtos.ContactFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contact_form")
@AllArgsConstructor
public class ContactForm {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @NotNull
    @Email
    private String email;

    private String name;

    @NotNull
    private String message;


    public ContactForm(String email, String name, String message){
        this.email = email;
        this.name = name;
        this.message = message;
    }
    public static ContactForm fromContactFormDto(ContactFormDto contactFormDto){
        return new ContactForm(contactFormDto.getEmail(), contactFormDto.getName(), contactFormDto.getMessage());
    }
}
