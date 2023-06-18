package de.jankorb.contactformhandler.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDto {


    private String email;

    private String name;

    private String subject;

    private String message;
}
