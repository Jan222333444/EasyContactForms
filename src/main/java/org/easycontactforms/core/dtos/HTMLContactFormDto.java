package org.easycontactforms.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HTMLContactFormDto extends ContactFormDto{

    private String redirect;
}
