package me.yarinlevi.qprivatevoices.commandhandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Map;

/**
 * @author YarinQuapi
 **/
public interface Command {
    void run(SlashCommandEvent event, Map<String, Object> args);

    String getName();

    boolean getAdministrative();

    void selfRegisterWithDiscord(JDA jda);
}
