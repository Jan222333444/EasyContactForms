package org.easycontactforms.core.pluginloader;

import lombok.extern.slf4j.Slf4j;
import org.easycontactforms.api.Plugin;
import org.easycontactforms.core.EasyContactFormsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Class for handling command line inputs
 */
@Slf4j
public class CommandHandler {

    /**
     * Currently used Spring Application Context
     */
    private ApplicationContext context;

    /**
     * JVM arguments for startup of service
     */
    private final String[] baseArgs;

    /**
     * List of all plugins ordered by priority
     */
    private List<Plugin> plugins;

    public CommandHandler(ApplicationContext context, String[] args){
        this.context = context;
        this.baseArgs = args;
        this.plugins = this.priorities();
    }

    /**
     * gets executed if new line is entered
     * @param command base command (everything before first white space)
     * @param args all command line arguments (including command)
     */
    public void onCommand(String command, String... args) {
        boolean internalCommand = internalCommands(command, args);
        if(!internalCommand){
            onCommandPlugins(command, args);
        }
    }

    /**
     * Sorts plugins by priority
     * @return list of all plugins loaded sorted by priority from Highest to Lowest
     */
    private List<Plugin> priorities(){
        return PluginStore.instance.plugins.values().stream().sorted(Comparator.comparingInt(plugin -> plugin.priority.value)).collect(Collectors.toList());
    }

    /**
     * executes plugin commands
     * @param command executed command
     * @param args command line arguments
     */
    private void onCommandPlugins(String command, String... args) {
        for (Plugin plugin : plugins) {
            try {
                boolean handled = plugin.onCommand(command, args);
                if (handled) {
                    break;
                }
            } catch (AbstractMethodError error) {
                log.warn("Plugin does not implement onCommand Method");
                log.warn(error.getMessage());
            }
        }
    }

    /**
     * executes basic internal commands from command line
     * @param command executed command
     * @param args command line arguments
     * @return true if command is internal, else false
     */
    private boolean internalCommands(String command, String... args)  {
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
        } else if (command.equalsIgnoreCase("shutdown")) {
            shutdown(args);
            return true;
        }
        return false;
    }

    /**
     * reloads all plugins from source
     */
    private void reloadPlugins(){
        String pluginsPath = "plugins";

        EasyContactFormsApplication.loadPlugins(pluginsPath);
        plugins = priorities();

        //Starts all plugins
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onStartup();
        }

        //Executes on load hook
        for (String key : PluginStore.instance.plugins.keySet()) {
            PluginStore.instance.plugins.get(key).onLoad();
        }
    }

    /**
     * logic for shutdown command
     * @param args command line arguments
     */
    private void shutdown(String... args){
        // standard execution
        if(args.length == 1){
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
            return;
        }
        // initiates shutdown immediately (like stop command)
        if(args[1].equalsIgnoreCase("now")){
            System.exit(0);
            return;
        }
        // initiates shutdown after given delay in seconds
        try{
            int delay = Integer.parseInt(args[1]);
            TimeUnit.SECONDS.sleep(delay);
            System.exit(0);
        } catch (NumberFormatException exception) {
            log.error("Cannot parse input. Argument 1 is not of type integer: {}", args[1]);
        } catch (InterruptedException e) {
            log.error("Something went wrong on Shutdown");
        }
    }
}
