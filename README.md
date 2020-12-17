# DiscordBot
## General
This is a Java written Discord bot based on the [javacord API](https://github.com/Javacord/Javacord).

## Download
This program is currently in its alpha phase and currently there are no prebuild files. It is planned to add them in the future, so the bot can move on to the beta phase and to releases.

## Common Mistakes
### No logger output
This can easily be fixed by going to the log4j2.xml file and changing the value for info
<br>
<code> 
\<Configuration status="INFO"><br>
    \<Appenders\><br>
        \<Console name="Console" target="SYSTEM_OUT"\><br>
            \<PatternLayout disableANSI="false" pattern="%highlight{%d{HH:mm:ss} \[%t] %-5level %logger{36}:\t %msg%n}{FATAL=red blink, ERROR=red, WARN=yellow bold, INFO=white, DEBUG=green bold, TRACE=blue}"/><br>
        </Console><br>
        <Console name="BotLevel" target="SYSTEM_OUT"><br>
            \<PatternLayout disableANSI="false" pattern="%highlight{%d{HH:mm:ss} \[%t] %-5level %logger{36}:\t %msg%n}{FATAL=red blink, ERROR=red, WARN=yellow bold, INFO=white, DEBUG=green bold, TRACE=blue} in shard %X"/><br>
        </Console><br>
    </Appenders><br>
</code>
<br>
to either white or another color, which is **Not** the background color of your terminal.
