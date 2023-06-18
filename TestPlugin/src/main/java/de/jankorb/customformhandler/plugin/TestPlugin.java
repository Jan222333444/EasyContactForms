package de.jankorb.customformhandler.plugin;

import de.jankorb.contactformhandler.api.Plugin;
import de.jankorb.contactformhandler.api.PluginFactory;
import de.jankorb.contactformhandler.api.models.ContactForm;
import de.jankorb.contactformhandler.api.models.ContactFormDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestPlugin implements Plugin {

    public List<PluginFactory> getPluginFactories(){
        ArrayList<PluginFactory> factories = new ArrayList<>();
        factories.add(new TestPluginFactory());
        return factories;
    }
    @Override
    public boolean onStartup() {
        System.out.println("Im started");
        return true;
    }

    @Override
    public boolean onLoad() {
        return true;
    }

    @Override
    public boolean onTeardown() {
        return true;
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
}
