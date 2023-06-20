package org.easycontactforms.core.repositories;

import org.easycontactforms.core.models.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to interact with database
 */
public interface ContactFormRepository extends JpaRepository<ContactForm, Integer> {


    public List<ContactForm> findByEmailSent(boolean emailSent);
}
