package org.easycontactforms.core;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.PluginFactory;
import org.easycontactforms.core.commandhandler.CommandHandlerThread;
import org.easycontactforms.core.pluginloader.PluginLoader;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
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

        new FirstStartupChecker().checkDirectories();
        loadPlugins(pluginsPath);

        //Starts all plugins
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onStartup();
        }
        //Starts Spring Boot server
        ApplicationContext context = SpringApplication.run(EasyContactFormsApplication.class, args);

        //Executes on load hook
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onLoad();
        }

        CommandHandlerThread handlerThread = new CommandHandlerThread(context, System.in, args);
        handlerThread.start();
    }

    public static void loadPlugins(String pluginsPath) {
        PluginLoader pluginLoader = new PluginLoader(new File(pluginsPath));
        pluginLoader.loadPlugins();

        Map<String, PluginFactory> factories = pluginLoader.getPluginFactories();

        for (String key : factories.keySet()) {
            PluginStore.instance.plugins.put(key, factories.get(key).build());
        }
    }

    @PreDestroy
    public static void teardown() {
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onTeardown();
        }
    }


}
