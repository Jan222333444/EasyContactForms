package org.easycontactforms.core.pluginloader;

import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.PluginFactory;
import org.easycontactforms.core.EasyContactFormsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.Map;

@Slf4j
public class CommandHandler {

    private ApplicationContext context;
    private String[] baseArgs;

    public CommandHandler(ApplicationContext context, String[] args){
        this.context = context;
        this.baseArgs = args;
    }

    public void onCommand(String command, String... args) {
        boolean internalCommand = internalCommands(command, args);
        if(!internalCommand){
            onCommandPlugins(command, args);
        }
    }

    private void onCommandPlugins(String command, String... args) {
        for (String key : PluginStore.instance.plugins.keySet()) {
            try {
                boolean handled = PluginStore.instance.plugins.get(key).onCommand(command, args);
                if (handled) {
                    break;
                }
            } catch (AbstractMethodError error) {
                log.warn("Plugin does not implement onCommand Method");
                log.warn(error.getMessage());
            }
        }
    }

    private boolean internalCommands(String command, String... args){
        if (command.equalsIgnoreCase("stop")) {
            System.exit(0);
            return true;
        } else if (command.equalsIgnoreCase("reloadplugins")) {
            reloadPlugins();
            return true;
        } else if (command.equalsIgnoreCase("reloadspring")) {
            SpringApplication.exit(context);
            this.context = SpringApplication.run(EasyContactFormsApplication.class, this.baseArgs);
            return true;
        } else if (command.equalsIgnoreCase("reload")) {
            SpringApplication.exit(context);
            this.context = SpringApplication.run(EasyContactFormsApplication.class, this.baseArgs);
            reloadPlugins();
            return true;
        }
        return false;
    }

    private void reloadPlugins(){
        String pluginsPath = "plugins";

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

        //Executes on load hook
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onLoad();
        }
    }
}
