package com.github.MaFeLP.bot;

import com.github.MaFeLP.Main;
import com.github.MaFeLP.bot.Listener.MessageListeners;
import com.github.MaFeLP.bot.Listener.ServerBecomesAvailableListeners;
import com.github.MaFeLP.cli.CLIHub;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import static java.lang.System.out;
import com.github.MaFeLP.settings.*;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;

import javax.swing.text.html.parser.Entity;

public class Init {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(Init.class);

    /**
     * Runs a started bot
     * @param props to use the same props of com.github.MaFeLP.settings.Props
     */
    public static void run(Props props) {
        logger.info("Setting up a Discord Bot instance");
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
        logger.info("Creating Bot instance");
        Props props = new Props();
        //Log in the bot and create api instance
        logger.info("Preparing bot instance");
        DiscordApi api = new DiscordApiBuilder()
                //Adds listeners
                .addListener(new MessageListeners())
                .addListener(new ServerBecomesAvailableListeners())
                .setToken(props.token)
                .setWaitForServersOnStartup(false)
                .setWaitForUsersOnStartup(false)
                .login()
                .join();
        logger.info("Bot instance created");

        //Displays bot token if enabled
        if (props.logToken) {
            logger.info("Bot token set to: " + props.token);
        }

        //Displays the bot invite link
        if (props.showInviteLink) {
            logger.info("Bot invite link is: " + api.createBotInvite());
        }

        logger.info("Updating bot activity");
        api.updateActivity(ActivityType.CUSTOM, props.defaultActivity);
        logger.info("Bot activity set to: " + api.getActivity());
        return api;
    }
}
