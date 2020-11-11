package com.github.MaFeLP;

import com.github.MaFeLP.settings.*;
import com.github.MaFeLP.bot.*;
import org.javacord.api.DiscordApi;

import static java.lang.System.err;
import static java.lang.System.out;

public class Main {

    public static void main (String[] args) {
        Props props = new Props(); //Settings to call

        Init.run(props);
    }

    /**
     * Shuts the bot down safely
     * @param api The Discord Bot instance
     */
    public static void shutdown (DiscordApi api) {
        //Shutdown sequence
        if (api != null) {
            out.println(Colors.BLUE + "Shutting the bot safely down..." + Colors.RESET);
            api.disconnect();
            out.println(Colors.GREEN_BOLD_BRIGHT + "==> Done!" + Colors.RESET);
        } else {
            err.println("No bot was running!");
        }

        out.println(Colors.BLUE + "Exiting Java Runtime with exit code 0..." + Colors.RESET);
        System.exit(0);
    }
}
