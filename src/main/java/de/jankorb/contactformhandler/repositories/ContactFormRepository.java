package de.jankorb.contactformhandler.repositories;

import de.jankorb.contactformhandler.models.ContactForm;
import org.springframework.data.repository.CrudRepository;

public interface ContactFormRepository extends CrudRepository<ContactForm, Integer> {
}
