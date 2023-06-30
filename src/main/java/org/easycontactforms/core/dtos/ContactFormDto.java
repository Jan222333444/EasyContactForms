package org.easycontactforms.core.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
