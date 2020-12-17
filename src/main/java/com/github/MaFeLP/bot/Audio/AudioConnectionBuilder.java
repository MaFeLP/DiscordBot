package com.github.MaFeLP.bot.Audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.concurrent.CompletableFuture;

public class AudioConnectionBuilder {
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(AudioConnectionBuilder.class);

    public AudioConnection connection;
    public AudioSource audioSource;

    public AudioConnectionBuilder(ServerVoiceChannel channel) {
        getConnection(channel);
    }

    public AudioConnectionBuilder(User user, Server server) {
        getConnection(user.getConnectedVoiceChannel(server).get());
    }

    public AudioConnectionBuilder(MessageCreateEvent messageCreateEvent) {
        CompletableFuture<Void> testi = getConnection(messageCreateEvent.getMessageAuthor().getConnectedVoiceChannel().get());
    }

    public CompletableFuture<Void> getConnection(ServerVoiceChannel serverVoiceChannel) {
        logger.info("Attempting to join Voice Channel " + serverVoiceChannel.getName());

        CompletableFuture<Void> test = serverVoiceChannel.connect().thenAccept(audioConnection -> {
            logger.info("Successfully joined Voice Channel " + serverVoiceChannel.getName());
            //audioSource = audioConnection.getAudioSource().get();

        }).exceptionally(e -> {
            logger.error("Cannot join Voice Channel " + serverVoiceChannel.getName() + " on server " + serverVoiceChannel.getServer().getName());
            logger.error("Maybe the bot does not have the permission to join this channel?");
            e.printStackTrace();
            return null;
        });
        return null;
    }
}
