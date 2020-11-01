package com.github.MaFeLP.bot.Listener;

import com.github.MaFeLP.settings.Colors;
import com.github.MaFeLP.settings.Props;
import org.javacord.api.event.server.ServerBecomesAvailableEvent;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

import static java.lang.System.out;

public class ServerBecomesAvailableListeners implements ServerBecomesAvailableListener {
    @Override
    public void onServerBecomesAvailable(ServerBecomesAvailableEvent event) {
        Props props = new Props();

        if (props.logServer) {
            out.println(Colors.GREEN_BOLD_BRIGHT + "Loaded server: \t\"" + Colors.WHITE + event.getServer().getName() + Colors.GREEN_BOLD_BRIGHT + "\"\n\t\t" +
                    Colors.WHITE + "id: \t\"" + event.getServer().getId() + Colors.GREEN_BOLD_BRIGHT + "\"" + Colors.RESET);
            if (props.logMembers) {
                out.println(Colors.YELLOW_UNDERLINED + "Members:" + Colors.CYAN);
                Object[] members = event.getServer().getMembers().toArray();

                for (int i=0; i < members.length; i++) {
                    out.println("\t" + members[i]);
                }

                out.println(Colors.RESET);
            }
        }
    }
}
