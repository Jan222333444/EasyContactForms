package org.easycontactforms.testplugin;

import org.easycontactforms.api.Plugin;
import org.easycontactforms.api.PluginFactory;

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
