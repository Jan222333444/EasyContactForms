package org.easycontactforms.core;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.PluginFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Main class of application
 * Handles startup and teardown of Plugins and application
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class EasyContactFormsApplication {

    public static void main(String[] args) {
        String pluginsPath = "plugins";

        new EasyContactFormsApplication().checkDirectories();
        PluginLoader pluginLoader = new PluginLoader(new File(pluginsPath));
        pluginLoader.loadPlugins();

        Map<String, PluginFactory> factories = pluginLoader.getPluginFactories();

        for(String key : factories.keySet()){
            PluginStore.instance.plugins.put(key, factories.get(key).build());
        }


        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onStartup();
        }
        SpringApplication.run(EasyContactFormsApplication.class, args);
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

    private void checkDirectories(){
        File pluginsDir = new File("plugins");
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            log.info("Plugin directory not found: " + pluginsDir + " | Adding directory");
            if(!pluginsDir.mkdirs()){
                log.error("Could not create plugins directory");
            }
        }
        File configDir = new File("config");
        if(!configDir.exists() || !configDir.isDirectory()){
            log.info("Config directory not found! Adding directory");
            if(!configDir.mkdirs()){
                log.error("Could not create config directory");
                throw new RuntimeException("Could not create config directory");
            }
            InputStream resource = getClass().getClassLoader().getResourceAsStream("/application.properties");
            if (resource == null) {
                throw new IllegalArgumentException("application.properties not found!");
            } else {

                try {
                    log.info("Adding standard configuration to config directory");
                    byte[] data = resource.readAllBytes();

                    OutputStream out = new FileOutputStream(configDir.getAbsolutePath() + System.getProperty("file.separator") + "application.properties");
                    out.write(data);
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
