package com.github.MaFeLP.settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Props {
    //All properties variables
    public final boolean logToken;
    public final boolean showInviteLink;
    public final boolean logServer;
    public final boolean logMembers;
    public final boolean enableCLI;
    public final boolean autoJoin;
    public final String token;
    public final String prefix;
    public final String botTestMessage;
    public final String defaultActivity;
    public final String loggingTemplate;
    public final long botID;
    public final long ownerID;
    public final long messageDeleteDelay;


    //Initialises all variables
    public Props() {
        logToken            = getBooleanProperty("logToken");
        showInviteLink      = getBooleanProperty("showInviteLink");
        logServer           = getBooleanProperty("logServer");
                            //Checks if logServer is enabled and only then displays members at startup
        logMembers          = getBooleanProperty("logMembers") && logServer;
        enableCLI           = getBooleanProperty("enableCLI");
        autoJoin            = getBooleanProperty("autoJoin");
        token               = getStringProperty("token");
        prefix              = getStringProperty("prefix");
        botTestMessage      = getStringProperty("botTestMessage");
        defaultActivity     = getStringProperty("defaultActivity");
        loggingTemplate     = getStringProperty("loggingTemplate");
        botID               = getLongProperty("botID");
        ownerID             = getLongProperty("ownerID");
        messageDeleteDelay  = getLongProperty("messageDeleteDelay");
    }

    /**
     * @param property property to get the String from
     * @return the value of the given property as String
     */
    private String getStringProperty(String property) {
        String out = null;
        try {
            FileInputStream fis = new FileInputStream("discordbot.properties");
            Properties properties = new Properties();
            properties.load(fis);

            out = properties.getProperty(property);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println();
            System.err.println("File discordbot.properties not found!");
            System.exit(2);
        }
        return out;
    }

    /**
     * @param property property to get the boolean from
     * @return the value of the given property as boolean
     */
    private boolean getBooleanProperty(String property) {
        boolean out = false;
        try {
            FileInputStream fis = new FileInputStream("discordbot.properties");
            Properties properties = new Properties();
            properties.load(fis);

            out = Boolean.parseBoolean(properties.getProperty(property));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println();
            System.err.println("File discordbot.properties not found!");
            System.exit(2);
        }

        return out;
    }

    /**
     * @param property property to get the Long from
     * @return the value of the given property as Long
     */
    private long getLongProperty(String property) {
        long out = 0;
        try {
            FileInputStream fis = new FileInputStream("discordbot.properties");
            Properties properties = new Properties();
            properties.load(fis);

            out = Long.parseLong(properties.getProperty(property));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println();
            System.err.println("File discordbot.properties not found or corrupted.");
            System.err.println("If this is your first run, ignore this message.\n" +
                    "Afterwards run .botID and .myID on a server and place the values in the config file.");
//            System.exit(2);
        }

        return out;
    }
}
