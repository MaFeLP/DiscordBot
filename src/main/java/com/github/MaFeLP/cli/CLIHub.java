package com.github.MaFeLP.cli;

import com.github.MaFeLP.Main;
import com.github.MaFeLP.settings.Colors;
import com.github.MaFeLP.settings.Props;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.Scanner;

import static com.github.MaFeLP.bot.Init.join;
import static java.lang.System.err;
import static java.lang.System.out;

public class CLIHub {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(CLIHub.class);

    public void startCLI(Props props) {
        logger.info("Starting command line interface");
        logger.info("Command line interface started. Use 'help' for help");

        Scanner keyboard = new Scanner(System.in);

        boolean shutdown = false;
        do {
            String input = keyboard.nextLine();
            String[] inputArray = input.split(" ");
            String command = inputArray[0].toLowerCase();

            //Determine command and calling corresponding method
            switch (command) {
                case "shutdown", "exit" -> shutdown = true;
                case "help" -> help(inputArray);
                //case "settings" -> settings(inputArray);
                case "join" -> join();
                case "disconnect" -> disconnect();
                case "servers" -> servers();
                default -> err.println("Command \"" + command + "\" is not a valid command! Use \"help\" for help.");
            }

            if (input.equalsIgnoreCase("shutdown")) {shutdown = true;}
        } while (!shutdown);

        //Shutdown
        keyboard.close();
        Main.shutdown();
    }

    private void servers() {
        out.println("" +
                "\n" +
                Colors.GREEN + "Joined Servers:\n" +
                Colors.YELLOW_UNDERLINED + "Server ID" + Colors.RESET + "\t\t" + Colors.YELLOW_UNDERLINED + "Server Name\n" +
                Colors.CYAN);

        for (DiscordApi api : Main.apis) {
            for (Server server : api.getServers()) {
                out.println(server.getId() + "\t" + server.getName());
            }
        }
    }

    private void help(String[] args) {
        if (args.length <= 1) {
            out.println("" +
                    "\n" +
                    Colors.GREEN + "Use \"help <command>\" to see detailed information about a specific command.\n\n" +
                    Colors.YELLOW_UNDERLINED + "Command" + Colors.RESET + "\t\t" + Colors.YELLOW_UNDERLINED + "Function\n" + Colors.RESET +
                    Colors.CYAN +
                    "shutdown\tends the program\n" +
                    //"settings\tdisplays current settings/info for a specific setting\n" +
                    "help\t\tdisplays this text\n" +
                    "join\t\tjoins the bot\n" +
                    "disconnect\tdisconnects the bot, but doesn't shut down the CLI" +
                    Colors.RESET);
            return;
        }

        switch (args[1].toLowerCase()) {
            case "help" -> out.println("Displays the help text.");
            case "shutdown" -> out.println("Safely stops the bot and ends the java program");
            case "join" -> out.println("Joins the bot to all servers it is a member of.");
            case "disconnect" -> out.println("Disconnects the bot from all servers it's a member of.");
            default -> err.println("No valid argument given.\n" +
                    "See \"help\" for all possible arguments/commands.");
        }
    }

    private void settings(String[] args) {
        if (args.length == 1) {
            out.println(Colors.RESET + Colors.PURPLE + "\n" +
                    "discordbot.properties is the settings file.\n\n" +
                    Colors.YELLOW_UNDERLINED + "Type" + Colors.RESET + "\t" + Colors.YELLOW_UNDERLINED + "Possible settings\n" +
                    Colors.RESET + Colors.CYAN +
                    "Boolean\ttrue, false\n" +
                    "String\tany type of characters/words\n" +
                    "Long\ta number\n\n" +
                    Colors.YELLOW_UNDERLINED + "Setting" + Colors.RESET + "\t\t\t" + Colors.YELLOW_UNDERLINED + "Type\n" +
                    Colors.RESET + Colors.CYAN +
                    "logToken\t\tBoolean\n" +
                    "token\t\t\tString\n" +
                    "showInviteLink\tBoolean\n" +
                    "prefix\t\t\tString\n" +
                    "ownerID\t\t\tLong\n" +
                    "botID\t\t\tLong\n" +
                    "botTestMessage\tString\n\n" +
                    Colors.GREEN + "Use \"settings <setting>\" to get more information about it.\n" +
                    Colors.RESET);
        }
    }

    private void disconnect() {
        for (DiscordApi api : Main.apis) {
            if (api != null) {
                out.println(Colors.BLUE + "Shutting the bot safely down..." + Colors.RESET);
                api.disconnect();
                out.println(Colors.GREEN_BOLD_BRIGHT + "==> Done!" + Colors.RESET);
            } else {
                err.println("Cannot disconnect the bot!\nNo bot running!\n\nUse join to join the bot!");
            }
        }

    }
}
