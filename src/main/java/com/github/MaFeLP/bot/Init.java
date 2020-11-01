package com.github.MaFeLP.bot;

import com.github.MaFeLP.bot.Listener.MessageListeners;
import com.github.MaFeLP.bot.Listener.ServerBecomesAvailableListeners;
import com.github.MaFeLP.cli.CLIHub;
import org.javacord.api.DiscordApi;
import static java.lang.System.out;
import com.github.MaFeLP.settings.*;
import org.javacord.api.DiscordApiBuilder;

public class Init {

    /**
     * Runs a started bot
     * @param props to use the same props of com.github.MaFeLP.settings.Props
     */
    public static void run(Props props) {
        DiscordApi api = null;

        if (props.autoJoin) {
            api = join();
        }

        //Starts the command line interface CLI if enabled
        if (props.enableCLI) {
            CLIHub cliHub = new CLIHub();
            cliHub.startCLI(api, props);
        }
    }

    public static DiscordApi join() {
        Props props = new Props();
        //Log in the bot and create api instance
        out.println(Colors.CYAN_BRIGHT + "Bot is being logged in..." + Colors.RESET);
        DiscordApi api = new DiscordApiBuilder()
                //Adds listeners
                .addListener(new MessageListeners())
                .addListener(new ServerBecomesAvailableListeners())
                .setToken(props.token)
                .setWaitForServersOnStartup(false)
                .login()
                .join();
        out.println(Colors.GREEN_BOLD_BRIGHT + "==> Done!" + Colors.RESET);

        //Displays bot token if enabled
        if (props.logToken) {
            out.println(Colors.GREEN_BRIGHT + "Bot token set to: " + Colors.YELLOW_BRIGHT + props.token + Colors.RESET);
        }

        //Displays the bot invite link
        if (props.showInviteLink) {
            out.println(Colors.GREEN_BRIGHT + "Bot invite link is: " + Colors.YELLOW_BRIGHT + api.createBotInvite() + Colors.RESET);
        }

        return api;
    }
}
