package org.easycontactforms.unittests;

import org.easycontactforms.api.Plugin;
import org.easycontactforms.core.commandhandler.CommandHandler;
import org.easycontactforms.core.commandhandler.CommandHandlerThread;
import org.easycontactforms.core.pluginloader.PluginStore;
import org.easycontactforms.unittests.testpluginclasses.Testplugin1;
import org.easycontactforms.unittests.testpluginclasses.Testplugin2;
import org.easycontactforms.unittests.testpluginclasses.Testplugin3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


public class CommandHandlerTest {

    @Test
    public void testPriorities(){
        Plugin pluginFirst = new Testplugin1();
        Plugin plugin2 = new Testplugin2();
        Plugin plugin3 = new Testplugin3();
        PluginStore.instance.plugins.put("first", pluginFirst);
        PluginStore.instance.plugins.put("middle", plugin2);
        PluginStore.instance.plugins.put("last", plugin3);
        CommandHandler handler = new CommandHandler(null, null);
        List<Plugin> result = handler.getPluginsByPriority();
        Assertions.assertTrue(result.get(0) instanceof Testplugin2);
        Assertions.assertTrue(result.get(1) instanceof Testplugin3);
        Assertions.assertTrue(result.get(2) instanceof Testplugin1);
    }

    @Test
    public void testInvalidShutdownTime(){
        CommandHandler handler = new CommandHandler(null, null);
        boolean result = handler.onCommand("shutdown", "shutdown","asdf");
        Assertions.assertTrue(result);
    }
    @Test
    public void testInvalidCommand(){
        CommandHandler handler = new CommandHandler(null, null);
        boolean result = handler.onCommand("asdfa", "asdfa","asdf");
        Assertions.assertFalse(result);
    }

    @Test
    public void testPluginCommand(){
        Plugin pluginFirst = new Testplugin1();
        PluginStore.instance.plugins.put("first", pluginFirst);
        CommandHandler handler = new CommandHandler(null, null);
        boolean result = handler.onCommand("plugintestcommand", "plugintestcommand");
        Assertions.assertTrue(result);
    }

    @Test
    public void testCommandHandlerThread() throws InterruptedException {
        InputStream stream = new ByteArrayInputStream("stopCommandHandler\n".getBytes());
        CommandHandlerThread thread = new CommandHandlerThread(null, stream, "");
        thread.start();
        thread.join();
    }
}
