package de.jankorb.contactformhandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDto {

    @NotNull
    @Email
    private String email;

    private String name;

    @NotNull
    private String message;
}
