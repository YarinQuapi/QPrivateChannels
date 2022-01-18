package me.yarinlevi.qprivatevoices;

import lombok.Getter;
import me.yarinlevi.qprivatevoices.commandhandler.CommandManager;
import me.yarinlevi.qprivatevoices.configuration.Configuration;
import me.yarinlevi.qprivatevoices.database.QDatabase;
import me.yarinlevi.qprivatevoices.listeners.ChannelManager;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class QPrivateVoices {
    @Getter private static QPrivateVoices instance;
    @Getter private static JDA jda;
    @Getter private static String version;

    /**
     * Configurations
     */
    @Getter private final Map<String, Configuration> configurations = new HashMap<>();

    @Getter private final QDatabase database;
    @Getter private final CommandManager commandManager;
    @Getter private final ChannelManager channelManager;

    protected QPrivateVoices() throws LoginException {
        instance = this;

        this.loadConfigs();

        String token = configurations.get("qbot").getString("token");

        version = configurations.get("qbot").getString("version");

        commandManager = new CommandManager();
        channelManager = new ChannelManager();

        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, "QVoiceOS v" + version))
                .addEventListeners(channelManager, commandManager);

        // Connection to discord
        jda = jdaBuilder.build();

        // Command registration
        commandManager.complete(jda);

        // Database initialization
        database = new QDatabase();

        Logger.info("Happy private chatting!");
    }

    private void loadConfigs() {
        Configuration config = Configuration.load("qbot.yml");
        configurations.put("qbot", config);
    }

    public String getDataFolder() {
        return System.getProperty("user.dir");
    }
}
