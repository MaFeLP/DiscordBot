package com.github.MaFeLP;

import com.github.MaFeLP.settings.*;
import com.github.MaFeLP.bot.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import java.util.ArrayList;

public class Main {

    public static volatile ArrayList<DiscordApi> apis;

    public static void main (String[] args) {
        Props props = new Props(); //Settings to call

        //Logging level: FATAL, ERROR, WARN, INFO, DEBUG
        //Setup logger
        System.setProperty("log4j.configurationFile",props.loggingTemplate); //Setting config file
        Logger logger = LogManager.getLogger(Main.class);
        logger.info("\tStarting DiscordBot version alpha-0.1 by MaFeLP");

        //Starts the program
        Init.run(props);
    }

    /**
     * Shuts the bot down safely
     */
    public static void shutdown () {
        //Shutdown sequence
        Logger logger = LogManager.getLogger(Main.class);
        logger.warn("\tBot and java runtime are being shut down");
        for (DiscordApi api : apis) {
            if (api != null) {
                logger.info("\tDisconnecting the bot safely");
                api.disconnect();
                logger.info("\tBot successfully disconnected");
            } else {
                logger.error("\tNo bot was running. Ignoring");
            }
        }

        logger.info("\tExiting Java Runtime with exit code 0");
        System.exit(0);
    }
}
