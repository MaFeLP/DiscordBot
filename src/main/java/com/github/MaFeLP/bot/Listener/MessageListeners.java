package com.github.MaFeLP.bot.Listener;

import com.github.MaFeLP.settings.Colors;
import com.github.MaFeLP.settings.Props;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.net.URL;

import static java.lang.System.out;

public class MessageListeners implements MessageCreateListener {
    public MessageListeners() {
        out.println(Colors.GREEN + "MessageListener enabled!" + Colors.RESET);
    }

    private MessageAuthor author;    //Who sent the message
    private String authorName;       //Name of whom sent the message
    private long authorID;           //ID of whom sent the message
    private TextChannel channel;     //Channel the message was sent in
    private String content;          //Content of the messages

    /**
     * Listens to all messages that are being sent on the server
     * and reacts to them.
     * @param e is the event which holds all specific information about the message
     */
    @Override
    public void onMessageCreate(MessageCreateEvent e) {
        Props props = new Props();

        //Check if message started with prefix
        if (!e.getMessageContent().startsWith(props.prefix)) {
            return;
        }

        //Available commands (only for check):
        String[] commands = {
          "disconnect", "shutdown", "exit",
          "myID",
          "botID",
          "invite"
        };

        //Variables for easier use
        author = e.getMessageAuthor();
        authorID = author.getId();
        authorName = author.getName();
        channel = e.getChannel();
        content = e.getMessageContent();

        //Checks if the bot has sent the test message
        if (content.equals(props.prefix + props.botTestMessage)) {
            botTestMessage(e);
            return;
        }

        //Splits the input message into an array, so it can be treated as a command with arguments
        String[] input = content.split(" ");

        //Check if command exists
        boolean commandExists = false;
        for (String s: commands) {
            if ((props.prefix + s).equalsIgnoreCase(input[0])) {
                out.println(authorName + " issued command " + input[0]);
                commandExists = true;
            }
        }
        if (!commandExists) {
            channel.sendMessage("Command \"" + input[0] + "\" doesn't exist!");
        }


        //Command disconnect
        if (content.equalsIgnoreCase(props.prefix + "disconnect") ||
            content.equalsIgnoreCase(props.prefix + "shutdown") ||
            content.equalsIgnoreCase(props.prefix + "exit")) { disconnect(e); }
        //Command myID
        if (content.equalsIgnoreCase(props.prefix + "myID")) { myID(e); }
        //Command botID
        if (content.equalsIgnoreCase(props.prefix + "botID")) { botID(e, props); }
        //Command invite
        if (content.equalsIgnoreCase(props.prefix + "invite")) { invite(e); }
    }

    /**
     * disconnects the bot after a message was sent
     * @param e is the event which holds all specific information about the message
     */
    private void disconnect(MessageCreateEvent e) {
        Props props =new Props();
        if (authorID != props.ownerID) {
            e.addReactionsToMessage(EmojiParser.parseToUnicode(":cry:"));
            new MessageBuilder()
                    .append("We are very sorry, but...\n")
                    .append("You don't have the permission to turn off the bot.:cry:")
                    .send(channel);
            return;
        }

        //Shutdown sequence
        e.addReactionsToMessage(EmojiParser.parseToUnicode(":wave:"));
        new MessageBuilder()
                .append("Good Bye! :wave:")
                .send(channel);

        out.println(Colors.BLUE + "Shutting the bot safely down..." + Colors.RESET);
        e.getApi().disconnect();
        out.println(Colors.GREEN_BOLD_BRIGHT + "==> Done!" + Colors.RESET);
        out.println(Colors.YELLOW_BRIGHT + "Shutdown was caused by: " + authorName);

        out.println(Colors.BLUE + "Exiting Java Runtime with exit code 0..." + Colors.RESET);
        System.exit(0);
    }

    /**
     * Gives the user his/her id
     * @param e MessageCreateEvent issued by the Message Sender
     */
    private void myID(MessageCreateEvent e) {
        e.getChannel().sendMessage("Id of user " + authorName + " is: " + authorID);
        out.println("Id of user " + authorName + " is: " + authorID);
    }

    private void botID(MessageCreateEvent e, Props props) {
        channel.sendMessage(props.prefix + props.botTestMessage);
    }

    private void botTestMessage(MessageCreateEvent e) {
        out.println("Bot ID is: "+ e.getMessageAuthor().getId());
        e.getMessage().delete();
        e.getChannel().sendMessage("Bot ID has been logged to the bot console!");
    }

    /**
     * Gives the user an invite link to invite the bot to other servers
     * @param e MessageCreateEvent issued by the Message Sender
     */
    private void invite(MessageCreateEvent e) {
        URL link = e.getMessageLink();

        EmbedBuilder reply = new EmbedBuilder()
                .setTitle(null)
                .setDescription(null)
                .setAuthor(author)
                .addField("You executed command:", content)
                .addField("Result:", e.getApi().createBotInvite())
                .setColor(Color.LIGHT_GRAY)
                .setFooter(null)
                ;

        new MessageBuilder()
                .setEmbed(reply)
                .append("Test")
                .send(channel);
    }
}
