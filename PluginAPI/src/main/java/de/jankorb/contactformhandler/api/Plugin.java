package de.jankorb.contactformhandler.api;

import de.jankorb.contactformhandler.api.models.ContactFormDto;
import de.jankorb.contactformhandler.api.models.ContactForm;

import java.util.Collections;
import java.util.List;

public interface Plugin {
    
    public PluginPriority priority = PluginPriority.LOW;

    default List<PluginFactory> getPluginFactories() {
        return Collections.emptyList();
    }

    /**
     * Executed on Startup of Server
     * @return true if successful; false on error
     */
    public boolean onStartup();

    /**
     * Executed when all plugins are loaded
     * @return true if successful; false on error
     */
    public boolean onLoad();

    /**
     * Executed before stopping the server
     * @return true if successful; false on error
     */
    public boolean onTeardown();

    /**
     * Is called before any processing happens
     * @param contactForm the contact form sent
     * @return true if processing is finished and other plugins can access; false cancels all other plugin executions after this plugin
     */
    public boolean beforeContactFormProcessing(ContactFormDto contactForm);

    /**
     * Is called after processing is finished
     * @param contactForm the contact form after processing
     * @return true if processing is finished and other plugins can access; false cancels all other plugin executions after this plugin
     */
    public boolean contactFormProcessed(ContactForm contactForm);

    public boolean onMailSent(ContactForm contactForm);
}
