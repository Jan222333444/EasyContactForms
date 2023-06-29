package org.easycontactforms.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HTMLContactFormDto extends ContactFormDto{

    /**
     * url/path for redirect after successful processing of data
     */
    private String redirect;

    /**
     * url/path for redirect if processing of data failed
     */
    private String redirectError;
}
