package org.easycontactforms.unittests.testpluginclasses;

import org.easycontactforms.api.Plugin;
import org.easycontactforms.api.PluginPriority;
import org.easycontactforms.api.models.ContactForm;
import org.easycontactforms.api.models.ContactFormDto;

public class Testplugin1 implements Plugin {

    PluginPriority priority = PluginPriority.LOWEST;
    @Override
    public boolean onStartup() {
        return false;
    }

    @Override
    public boolean onLoad() {
        return false;
    }

    @Override
    public boolean onTeardown() {
        return false;
    }

    @Override
    public boolean beforeContactFormProcessing(ContactFormDto contactForm) {
        return false;
    }

    @Override
    public boolean contactFormProcessed(ContactForm contactForm) {
        return false;
    }

    @Override
    public boolean onMailSent(ContactForm contactForm) {
        return false;
    }

    @Override
    public boolean onCommand(String command, String... args) {
        return false;
    }
}
