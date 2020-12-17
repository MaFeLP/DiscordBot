package com.github.MaFeLP.bot;

import com.github.MaFeLP.Main;
import com.github.MaFeLP.bot.Listener.MessageListeners;
import com.github.MaFeLP.bot.Listener.ServerBecomesAvailableListeners;
import com.github.MaFeLP.cli.CLIHub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import com.github.MaFeLP.settings.*;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import java.util.Collection;

public class Init {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(Init.class);

    /**
     * Runs a started bot
     * @param props to use the same props of com.github.MaFeLP.settings.Props
     */
    public static void run(Props props) {
        logger.info("Setting up a Discord Bot instance");

        if (props.autoJoin) {
            join();
        }

        //Starts the command line interface CLI if enabled
        if (props.enableCLI) {
            CLIHub cliHub = new CLIHub();
            cliHub.startCLI(props);
        }
    }

    public static void join() {
        logger.info("Creating Bot instance(s)");
        Props props = new Props();
        //Log in the bot and create api shards
        logger.info("Preparing bot instance(s)");

        new DiscordApiBuilder()
                .setWaitForServersOnStartup(false)
                .setWaitForUsersOnStartup(false)
                .setToken(props.token)
                .setRecommendedTotalShards().join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                    .thenAcceptAsync(Init::onShardLogin)
                    //.exceptionally(ExceptionLogger.get())
                );

        logger.info("Bot instance created");

        //Displays bot token if enabled
        if (props.logToken) {
            logger.info("Bot token set to: " + props.token);
        }

    }

    private static void onShardLogin(DiscordApi api) {
        Props props = new Props();

        logger.info("Shard " + api.getCurrentShard() + " logged in");

        api.addListener(new MessageListeners());
        api.addListener(new ServerBecomesAvailableListeners());

        Collection<Server> servers = api.getServers();
        for (Server server : servers) {
            logger.info("Joined Sever " + server.getName() + " with id " + server.getId());
        }

        logger.info("Updating bot activity");
        api.updateActivity(ActivityType.CUSTOM, props.defaultActivity);
        logger.info("Bot activity set to: " + api.getActivity());

        Main.apis.add(api);
    }
}
