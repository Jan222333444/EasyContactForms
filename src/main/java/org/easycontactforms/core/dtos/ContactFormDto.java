package org.easycontactforms.core.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "ContactFormDto")
public class ContactFormDto {

    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Has to be a valid email address")
    @JacksonXmlProperty(localName="email")
    private String email;

    @JacksonXmlProperty(localName="name")
    private String name;

    @JacksonXmlProperty(localName = "subject")
    private String subject;

    @NotNull
    @NotEmpty(message = "Message cannot be empty")
    @JacksonXmlProperty(localName="message")
    private String message;
}
