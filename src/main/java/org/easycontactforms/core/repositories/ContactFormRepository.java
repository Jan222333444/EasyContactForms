package org.easycontactforms.core.repositories;

import org.easycontactforms.core.models.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactFormRepository extends JpaRepository<ContactForm, Integer> {


    public List<ContactForm> findByEmailSent(boolean emailSent);
}
