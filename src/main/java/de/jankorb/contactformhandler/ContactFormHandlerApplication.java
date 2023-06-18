package de.jankorb.contactformhandler;

import de.jankorb.contactformhandler.api.PluginFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Map;

@SpringBootApplication
public class ContactFormHandlerApplication {

    public static void main(String[] args) {
        String pluginsPath = "plugins";
        PluginLoader pluginLoader = new PluginLoader(new File(pluginsPath));
        pluginLoader.loadPlugins();

        Map<String, PluginFactory> factories = pluginLoader.getPluginFactories();

        for(String key : factories.keySet()){
            PluginStore.instance.plugins.put(key, factories.get(key).build());
        }
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).onStartup();
        }
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).onLoad();
        }
        SpringApplication.run(ContactFormHandlerApplication.class, args);
        for(String key : PluginStore.instance.plugins.keySet()){
            PluginStore.instance.plugins.get(key).onTeardown();
        }
    }

}
