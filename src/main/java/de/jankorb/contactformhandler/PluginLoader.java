package de.jankorb.contactformhandler;

import de.jankorb.pluginapi.Plugin;
import de.jankorb.pluginapi.PluginFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public class PluginLoader {
    private final Map<String, PluginFactory> fooFactoryMap = new HashMap<>();
    private final File pluginsDir;
    private final AtomicBoolean loading = new AtomicBoolean();

    public PluginLoader(final File pluginsDir) {
        this.pluginsDir = pluginsDir;
    }

    public void loadPlugins() {
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            System.err.println("Skipping Plugin Loading. Plugin dir not found: " + pluginsDir);
            return;
        }

        if (loading.compareAndSet(false, true)) {
            final File[] files = requireNonNull(pluginsDir.listFiles());
            for (File pluginDir : files) {
                if (pluginDir.isDirectory()) {
                    loadPlugin(pluginDir);
                }
            }
        }
    }

    private void loadPlugin(final File pluginDir) {
        System.out.println("Loading plugin: " + pluginDir);
        final URLClassLoader pluginClassLoader = createPluginClassLoader(pluginDir);
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            for (Plugin plugin : ServiceLoader.load(Plugin.class, pluginClassLoader)) {
                installPlugin(plugin);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }


    private void installPlugin(final Plugin plugin) {
        System.out.println("Installing plugin: " + plugin.getClass().getName());
        for (PluginFactory f : plugin.getPluginFactories()) {
            fooFactoryMap.put(f.getName(), f);
        }
    }

    private URLClassLoader createPluginClassLoader(File dir) {
        final URL[] urls = Arrays.stream(Optional.ofNullable(dir.listFiles()).orElse(new File[]{}))
                .sorted()
                .map(File::toURI)
                .map(this::toUrl)
                .toArray(URL[]::new);

        return new PluginClassLoader(urls, getClass().getClassLoader());
    }

    private URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public PluginFactory getPluginFactory(String name) {
        return fooFactoryMap.get(name);
    }
}
