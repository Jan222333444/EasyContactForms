package org.easycontactforms.core;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.PluginFactory;
import org.easycontactforms.core.pluginloader.CommandHandler;
import org.easycontactforms.core.pluginloader.PluginLoader;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class of application
 * Handles startup and teardown of Plugins and application
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class EasyContactFormsApplication {
    public static void main(String[] args) throws IOException {
        String pluginsPath = "plugins";

        new FirstStartupChecker().checkDirectories();
        PluginLoader pluginLoader = new PluginLoader(new File(pluginsPath));
        pluginLoader.loadPlugins();

        Map<String, PluginFactory> factories = pluginLoader.getPluginFactories();

        for (String key : factories.keySet()) {
            PluginStore.instance.plugins.put(key, factories.get(key).build());
        }

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

        //Waiting and processing commands from std in
        Scanner scanner = new Scanner(System.in);
        CommandHandler handler = new CommandHandler(context, args);
        while (true) {
            String input = scanner.next();
            String[] arguments = input.split(" ");
            String command = arguments[0];
            handler.onCommand(command, arguments);

        }
    }

    @PreDestroy
    public static void teardown() {
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onTeardown();
        }
    }


}
