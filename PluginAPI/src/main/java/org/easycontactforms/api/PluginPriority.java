package org.easycontactforms.api;

public enum PluginPriority {
    LOWEST(0), LOW(1), MIDDLE(2), HIGH(3), HIGHEST(4);

    public final int value;

    private PluginPriority(int value){
        this.value = value;
    }
}
