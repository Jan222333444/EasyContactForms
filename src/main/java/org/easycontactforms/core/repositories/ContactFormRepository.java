package org.easycontactforms.core.repositories;

import org.easycontactforms.core.models.ContactForm;
import org.springframework.data.repository.CrudRepository;

public interface ContactFormRepository extends CrudRepository<ContactForm, Integer> {
}
