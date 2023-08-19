package org.easycontactforms.core.commandhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Thread for processing command line inputs
 */
@Slf4j
public class CommandHandlerThread extends Thread {

    private final ApplicationContext context;

    private final String[] baseArgs;

    private final InputStream inputStream;

    public CommandHandlerThread(ApplicationContext context, InputStream inputStream, String... baseArgs){
        this.context = context;
        this.baseArgs = baseArgs;
        this.inputStream = inputStream;
    }
    @Override
    public void run() {
        //Waiting and processing commands from std in
        Scanner scanner = new Scanner(inputStream);
        CommandHandler handler = new CommandHandler(context, baseArgs);
        while (true) {
            String input = scanner.nextLine();
            String[] arguments = input.split(" ");
            String command = arguments[0];
            boolean result = handler.onCommand(command, arguments);
            if (!result) {
                if(command.equalsIgnoreCase("stopCommandHandler")){
                    break;
                }
                log.error("Could not recognize command");
            }
        }
        log.info("Stopped command handler!");
    }
}
