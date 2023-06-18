package de.jankorb.customformhandler.plugin;

import de.jankorb.contactformhandler.api.Plugin;
import de.jankorb.contactformhandler.api.PluginFactory;

public class TestPluginFactory implements PluginFactory {
    @Override
    public String getName() {
        return "TestPlugin";
    }

    @Override
    public Plugin build() {
        return new TestPlugin();
    }
}
