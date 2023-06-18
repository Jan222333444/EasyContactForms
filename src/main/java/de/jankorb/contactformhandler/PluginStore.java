package de.jankorb.contactformhandler;

import de.jankorb.contactformhandler.api.Plugin;

import java.util.HashMap;
import java.util.Map;

public enum PluginStore {
    instance;
    public Map<String, Plugin> plugins = new HashMap<>();
}
