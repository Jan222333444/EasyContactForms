package org.easycontactforms.testplugin;

import org.easycontactforms.api.Plugin;
import org.easycontactforms.api.PluginFactory;
import org.easycontactforms.api.models.ContactForm;
import org.easycontactforms.api.models.ContactFormDto;

import java.util.ArrayList;
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
        System.out.println("on load");
        return true;
    }

    @Override
    public boolean onTeardown() {
        System.out.println("Teardown");
        return true;
    }

    @Override
    public boolean beforeContactFormProcessing(ContactFormDto contactForm) {
        System.out.println("Before Processing");
        return false;
    }

    @Override
    public boolean contactFormProcessed(ContactForm contactForm) {
        System.out.println("Processed");
        return false;
    }

    @Override
    public boolean onMailSent(ContactForm contactForm) {
        System.out.println("Mail Sent");
        return false;
    }

    @Override
    public boolean onCommand(String command, String... args) {
        return false;
    }
}
