package org.easycontactforms.core;

import org.easycontactforms.api.Plugin;

import java.util.HashMap;
import java.util.Map;

public enum PluginStore {
    instance;
    public Map<String, Plugin> plugins = new HashMap<>();
}
