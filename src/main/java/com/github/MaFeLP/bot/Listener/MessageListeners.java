package com.github.MaFeLP.bot.Listener;

import com.github.MaFeLP.Main;
import com.github.MaFeLP.cli.CLIHub;
import com.github.MaFeLP.settings.Colors;
import com.github.MaFeLP.settings.Props;
import com.vdurmont.emoji.EmojiParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.err;
import static java.lang.System.out;

public class MessageListeners implements MessageCreateListener {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(MessageListeners.class);

    public MessageListeners() {
        logger.info("Enabling Message Listener");
        logger.info("Message Listener enabled");
    }

    private MessageAuthor author;    //Who sent the message
    private String authorName;       //Name of whom sent the message
    private long authorID;           //ID of whom sent the message
    private TextChannel channel;     //Channel the message was sent in

    /**
     * Listens to all messages that are being sent on the server
     * and reacts to them.
     * @param e is the event which holds all specific information about the message
     */
    @Override
    public void onMessageCreate(MessageCreateEvent e) {
        Props props = new Props();
        logger.debug("A message was sent to channel " + channel + " by " + authorName);

        //Check if message started with prefix
        if (!e.getMessageContent().startsWith(props.prefix)) {
            logger.debug("Message was not a command.");
            return;
        } else {
            logger.debug("Message is a command");
        }

        //Available commandList (only for check):
        String[] commandList = {
          "help",
          "disconnect", "shutdown", "exit",
          "myID",
          "botID",
          "invite",
          "botinvite",
          "test"
        };

        //Variables for easier use
        author = e.getMessageAuthor();
        authorID = author.getId();
        authorName = author.getName();
        channel = e.getChannel();
        //Content of the messages
        String content = e.getMessageContent();

        //Checks if the bot has sent the test message
        if (content.equals(props.prefix + props.botTestMessage)) {
            logger.info("The bot has sent a test message to " + channel);
            botTestMessage(e);
            return;
        }

        logger.debug("Converting message to command...");
        //Splits the input message into an array, so it can be treated as a command with arguments
        String[] input = content.split(" ");

        String command = input[0];
        logger.debug("Command is: " + command);

        //Check if command exists
        boolean commandExists = false;
        for (String s: commandList) {
            if ((props.prefix + s).equalsIgnoreCase(command)) {
                logger.info(authorName + " issued command " + command);
                commandExists = true;
            }
        }
        if (!commandExists) {
            logger.error("Command " + input[0] + " doesn't exist! (Caused by " + authorName + ")");
            channel.sendMessage("Command \"" + input[0] + "\" doesn't exist!");
        }

        //Command help
        if (command.equalsIgnoreCase(props.prefix + "help")) { help(e, props); }
        //Command disconnect
        if (command.equalsIgnoreCase(props.prefix + "disconnect") ||
            command.equalsIgnoreCase(props.prefix + "shutdown") ||
            command.equalsIgnoreCase(props.prefix + "exit")) { disconnect(e); }
        //Command myID
        if (command.equalsIgnoreCase(props.prefix + "myID")) { myID(e, props); }
        //Command botID
        if (command.equalsIgnoreCase(props.prefix + "botID")) { botID(e); }
        //Command invite
        if (input[0].equalsIgnoreCase(props.prefix + "invite")) { invite(e, props); }
        //Command botInvite
        if (input[0].equalsIgnoreCase(props.prefix + "botinvite")) { botInvite(e, props); }
        //TestCommand
        if (input[0].equalsIgnoreCase(props.prefix + "test")) { test(e, props); }
    }
    /**
     * Prints the help embed to the channel
     * @param e MessageCreateEvent issued by the Message Sender
     * @param props Props class for dealing with a properties file
     */
    private void help(MessageCreateEvent e, Props props) {
        logger.info("Sending help embed to channel " + channel);

        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Help")
                .setDescription("The help of this Discord Bot!")
                .setAuthor(e.getApi().getYourself()) // or: .setAuthor(author)
                .addField("Command", "What it does")
                .addInlineField(props.prefix + "help","displays this text")
                .addInlineField(props.prefix + "myID","sends you your personal ID-number")
                .addInlineField(props.prefix + "invite","gets you a server invite link")
                .setColor(Color.green)
                .setFooter(null)
        ;
        if (author.isServerAdmin()) {
            //Admin-Help
            reply = new EmbedBuilder()
                    .setTitle("Help")
                    .setDescription("The help of this Discord Bot!")
                    .setAuthor(e.getApi().getYourself()) // or: .setAuthor(author)
                    .addField("Command", "What it does")
                    .addInlineField(props.prefix + "help","displays this text")
                    .addInlineField(props.prefix + "myID","sends you your personal ID-number")
                    .addInlineField(props.prefix + "exit","shuts down the bot")
                    .addInlineField(props.prefix + "botID","prints the bot ID to the console")
                    .addInlineField(props.prefix + "invite","gets you a server invite link")
                    .addInlineField(props.prefix + "botInvite","gets you a bot invite link")
                    .setColor(Color.green)
                    .setFooter(null)
                    ;
        }

        new MessageBuilder()
                .setEmbed(reply)
                .send(channel);
    }

    /**
     * disconnects the bot after a message was sent
     * @param e is the event which holds all specific information about the message
     */
    private void disconnect(MessageCreateEvent e) {
        Props props = new Props();
        if (authorID != props.ownerID) {
            e.addReactionsToMessage(EmojiParser.parseToUnicode(":cry:"));
            new MessageBuilder()
                    .append("We are very sorry, but...\n")
                    .append("You don't have the permission to turn off the bot.:cry:")
                    .send(channel);
            logger.warn("User " + authorName + " tried to shut down the bot! Permission denied.");
            return;
        }

        logger.warn("A bot shutdown was caused by " + authorName + "!");

        e.addReactionsToMessage(EmojiParser.parseToUnicode(":wave:"));

        logger.debug("Sending shutdown countdown embed to channel " + channel);
        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Shutdown")
                .setDescription("The bot is being shut down...")
                .setAuthor(author)
                .addField("Good Bye", EmojiParser.parseToUnicode(":wave:"))
                .setColor(Color.RED)
                .setFooter("Waiting " + props.messageDeleteDelay + " seconds.")
                ;

        CompletableFuture<Message> msg = new MessageBuilder()
                .setEmbed(reply)
                .send(channel)
                ;

        //Shuts down the bot after five seconds
        try {
            for (long i = props.messageDeleteDelay; i > 0; i--) {
                msg.get().edit(reply.setFooter("Shutting down in " + i + " seconds."));
                logger.debug("Shutdown timer is no at: " + i);
                TimeUnit.SECONDS.sleep(1);
            }
            msg.get().edit(reply.setFooter("Shutting down..."));
        } catch (InterruptedException | ExecutionException interruptedException) {
            //interruptedException.printStackTrace();
            logger.error("Countdown message already deleted or not reachable!");
            logger.error("Error code:\n" + interruptedException.toString());
        }

        //Shutdown sequence
        Main.shutdown(e.getApi());
    }

    /**
     * Gives the user his/her id
     * @param e MessageCreateEvent issued by the Message Sender
     * @param props Props class for dealing with a properties file
     */
    private void myID(MessageCreateEvent e, Props props) {
        logger.info("Id of user " + authorName + " is: " + authorID);

        //Sending the userID via private message
        logger.debug("Sending user " + authorName + " his id (" + authorID + ") via direct message.");
        EmbedBuilder dmEmbed = new EmbedBuilder()
                .setTitle("Your ID - Command")
                .setDescription("Displays your ID and deletes message.")
                .setAuthor(author)
                .addField("Your ID", authorID + "")
                .addField("Sent on Server", e.getMessage().getServer().get().getName())
                .setColor(Color.ORANGE)
                .setFooter(null)
                ;
        User user = e.getMessageAuthor().asUser().get();
        user.sendMessage(dmEmbed);

        //Sending an info into the server chat, that the id has been sent via dm.
        logger.debug("Sending info embed to server, that id has been sent via direct message");
        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Your ID")
                .setDescription(null)
                .setAuthor(author)
                .addField("Your ID", "Your ID has been sent to you via Direct Message!")
                .setColor(Color.ORANGE)
                .setFooter("Type " + props.prefix + "help for help!")
                ;

        CompletableFuture<Message> msg = new MessageBuilder()
                .setEmbed(reply)
                .send(channel)
                ;
        //Deletes the messages after 15 seconds.
        deleteMessage(e, reply, msg, -1, true);
    }

    /**
     * Prints out the bot ID to the console and send a corresponding message.
     * @param e MessageCreateEvent issued by the Message Sender
     */
    private void botID(MessageCreateEvent e) {
        logger.info("Bot ID is: "+ e.getApi().getYourself().getIdAsString());

        logger.debug("Info message has been sent to the channel, that id has been logged to console");
        EmbedBuilder reply = new EmbedBuilder()
                .setTitle(null)
                .setDescription(null)
                .setAuthor(e.getApi().getYourself())
                .addField("Bot ID", "The bot ID has been logged to the console!")
                .setColor(Color.LIGHT_GRAY)
                .setFooter(null)
                ;
        new MessageBuilder()
                .setEmbed(reply)
                .send(channel)
        ;
    }

    /**
     *  This function doesn't have a use right now,
     *  but it reacts to the bot test message, specified in the properties.
     * @param e MessageCreateEvent issued by the Message Sender
     */
    private void botTestMessage(MessageCreateEvent e) {
        logger.info("The bot has sent a test message.");
        e.getMessage().delete();
    }

    /**
     * Gives the user a link to invite other users to this server
     * @param e MessageCreateEvent issued by the Message Sender
     * @param props Props class for dealing with a properties file
     */
    private void invite(MessageCreateEvent e, Props props) {
        if (e.isPrivateMessage()) {
            errorOnDM(e);
            return;
        }

        ServerTextChannel serverTextChannel = e.getServerTextChannel().get();
        Invite inviteLink = new InviteBuilder(serverTextChannel)
                .setMaxUses(1)
                .setMaxAgeInSeconds(60*10)
                .create().join();

        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Invite")
                .setDescription("Server invitation link")
                .setAuthor(e.getApi().getYourself())
                .addField("Your server invite link is: ",  inviteLink.getUrl() + "")
                .addField("Uses", "This is a onetime use only link. Use it wisely!")
                .addField("Expires", "This Link is going to expire in 10 Minutes  " + EmojiParser.parseToUnicode(":scream:"))
                .setColor(Color.YELLOW)
                .setFooter("To create a bot invitation link use " + props.prefix + "botinvite")
                ;

        new MessageBuilder()
                .setEmbed(reply)
                .send(channel);
    }

    /**
     * Gives the user an invite link to invite the bot to other servers
     * @param e MessageCreateEvent issued by the Message Sender
     * @param props Props class for dealing with a properties file
     */
    private void botInvite(MessageCreateEvent e, Props props) {
        if (e.getMessageAuthor().getId() != props.ownerID) {
            errorOnPermission(e);
        }
        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Bot invitation")
                .setDescription("A link to invite this bot to your server")
                .setAuthor(e.getApi().getYourself())
                .addField("Link", e.getApi().createBotInvite())
                .setColor(Color.YELLOW)
                .setFooter("To create an invitation to this server use " + props.prefix + " invite")
                ;

        new MessageBuilder()
                .setEmbed(reply)
                .send(channel);
    }

    /**
     * deltes a Message after a certain amount of time
     * @param e The MessageCreateEvent created by sending a Message
     * @param embedToChangeDescription The EmbedBuilder of the message, which the footer should be changed to set the description.
     * @param message The CompletableFuture\<Message\> which is
     * @param messageDeleteDelay Delay after the message should be deleted. If set to -1, default value of discordbot.properties is used.
     * @param deleteOriginalMessage States if the command-message should be deleted
     */
    private void deleteMessage(MessageCreateEvent e,
                               EmbedBuilder embedToChangeDescription,
                               CompletableFuture<Message> message,
                               long messageDeleteDelay,
                               boolean deleteOriginalMessage) {
        Props props = new Props();
        if (messageDeleteDelay < 0) {
            messageDeleteDelay = props.messageDeleteDelay;
        }

        try {
            for (long i = messageDeleteDelay; i > 0; i--) {
                message.get().edit(embedToChangeDescription.setFooter("Deleting in " + i + " seconds."));
                TimeUnit.SECONDS.sleep(1);
            }
            message.get().delete();

            if (deleteOriginalMessage) {
                e.getMessage().delete();
            }
        } catch (InterruptedException | ExecutionException interruptedException) {
            interruptedException.printStackTrace();
            err.println("Message already deleted or not reachable!");
        }
    }

    private void test(MessageCreateEvent e, Props props) {
        channel.sendMessage("Test @" + authorID + "");
    }

    /**
     * Send the user a message, that this command can't be used in DMs
     * @param e The MessageCreateEvent created by sending a Message
     */
    private void errorOnDM(MessageCreateEvent e) {
        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("Error")
                .setDescription("Direct Messages Error!")
                .setAuthor(author)
                .addField("Error description", "You cannot create an invite link to a personal chat!\nUse this command on servers only!")
                .setColor(Color.RED)
                .setFooter(null)
                ;

        new MessageBuilder()
                .setEmbed(reply)
                .send(channel);
    }

    private void errorOnPermission(MessageCreateEvent e) {
        //TODO add permissions required Error
    }
}

/* Embed template:
EmbedBuilder reply = new EmbedBuilder()
                .setTitle(null)
                .setDescription(null)
                .setAuthor(e.getApi().getYourself()) // or: .setAuthor(author)
                .addField("Name", "value")
                .setColor(Color.LIGHT_GRAY)
                .setFooter(null)
                ;

        new MessageBuilder()
                .setEmbed(reply)
                .send(channel);
 */