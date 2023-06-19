package org.easycontactforms.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDto {

    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Has to be a valid email address")
    private String email;

    private String name;

    private String subject;

    @NotNull
    @NotEmpty(message = "Message cannot be empty")
    private String message;
}
