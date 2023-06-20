package org.easycontactforms.core;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.PluginFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Map;
@Slf4j
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


        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onStartup();
        }
        SpringApplication.run(ContactFormHandlerApplication.class, args);
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onLoad();
        }
    }

    @PreDestroy
    public static void teardown(){
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onTeardown();
        }
    }

}
