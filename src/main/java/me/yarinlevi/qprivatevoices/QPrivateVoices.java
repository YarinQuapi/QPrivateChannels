package me.yarinlevi.qprivatevoices;

import lombok.Getter;
import me.yarinlevi.qprivatevoices.commandhandler.CommandManager;
import me.yarinlevi.qprivatevoices.configuration.Configuration;
import me.yarinlevi.qprivatevoices.listeners.ChannelListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
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

    @Getter private CommandManager commandManager;

    protected QPrivateVoices() throws LoginException {
        instance = this;

        this.loadConfigs();

        String token = configurations.get("qbot").getString("token");

        version = configurations.get("qbot").getString("version");

        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, "QVoiceOS v" + version))
                .addEventListeners(new ChannelListener());

        jda = jdaBuilder.build();

        commandManager = new CommandManager(jda);
    }

    protected void loadConfigs() {
        Configuration config = Configuration.load("qbot.yml");
        configurations.put("qbot", config);
    }
}
