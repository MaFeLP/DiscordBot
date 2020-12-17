package com.github.MaFeLP.bot.Listener;

import com.github.MaFeLP.bot.Init;
import com.github.MaFeLP.settings.Colors;
import com.github.MaFeLP.settings.Props;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.server.ServerBecomesAvailableEvent;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

import static java.lang.System.err;
import static java.lang.System.out;

public class ServerBecomesAvailableListeners implements ServerBecomesAvailableListener {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(ServerBecomesAvailableListeners.class);

    public ServerBecomesAvailableListeners() {
        logger.info("Enabling Server Becomes Available Listener");
        logger.info("Server Becomes Available Listener enabled");
    }

    @Override
    public void onServerBecomesAvailable(ServerBecomesAvailableEvent event) {
        Props props = new Props();

        out.println("Test");

        if (props.logServer) {
            /*out.println(Colors.GREEN_BOLD_BRIGHT + "Loaded server: \t\"" + Colors.WHITE + event.getServer().getName() + Colors.GREEN_BOLD_BRIGHT + "\"\n\t\t" +
                    Colors.WHITE + "id: \t\"" + event.getServer().getId() + Colors.GREEN_BOLD_BRIGHT + "\"" + Colors.RESET);
            if (props.logMembers) {
                out.println(Colors.YELLOW_UNDERLINED + "Members:" + Colors.CYAN);
                Object[] members = event.getServer().getMembers().toArray();

                for (int i=0; i < members.length; i++) {
                    out.println("\t" + members[i]);
                }

                out.println(Colors.RESET);
            }*/

            logger.info("Joined Server " + event.getServer().getName() + " with id " + event.getServer().getIdAsString());
        }
    }
}
