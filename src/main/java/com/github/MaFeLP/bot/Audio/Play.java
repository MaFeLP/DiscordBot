package com.github.MaFeLP.bot.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;

import java.net.URI;

public class Play extends Thread{
    //Initialises a logger for this class
    private static final Logger logger = LogManager.getLogger(Play.class);
    private String link;
    private DiscordApi api;
    private ServerVoiceChannel channel;

    public Play(String link, DiscordApi api, ServerVoiceChannel channel) {
        this.link = link;
        this.api = api;
        this.channel = channel;
    }

    @Override
    public void run() {
        logger.info("Attempting to join Voice Channel " + channel.getName());

        channel.connect().thenAccept(audioConnection -> {
            logger.info("Successfully joined Voice Channel " + channel.getName());

            // Create a player manager
            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();

            // Create an audio source and add it to the audio connection's queue
            AudioSource source = new LavaPlayerAudioSource(api, player);
            audioConnection.setAudioSource(source);

            // You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
            playerManager.loadItem(link, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    player.playTrack(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        player.playTrack(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    // Notify the user that everything exploded
                }
            });
        }).exceptionally(e -> {
            logger.error("Cannot join Voice Channel " + channel.getName() + " on server " + channel.getServer().getName());
            logger.error("Maybe the bot does not have the permission to join this channel?");
            e.printStackTrace();
            return null;
        });
    }
}
