package de.jankorb.contactformhandler.api;

public interface PluginFactory {

    public String getName();

    public Plugin build();
}
