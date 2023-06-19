package org.easycontactforms.api;

public interface PluginFactory {

    public String getName();

    public Plugin build();
}
