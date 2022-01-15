package me.yarinlevi.qprivatevoices.commandhandler;

import me.yarinlevi.qprivatevoices.commands.UserAdd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commandMap = new HashMap<>();

    private final JDA instance;

    public CommandManager(JDA jda) {
        this.instance = jda;

        this.registerCommand(new UserAdd());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Map<String, Object> commandArguments = new HashMap<>();

        commandArguments.put("channel", event.getChannel());
        commandArguments.put("guild", event.getGuild());
        commandArguments.put("user", event.getUser());
        commandArguments.put("pure", event.getCommandPath());

        commandMap.get(event.getCommandId()).run(commandArguments);
    }

    protected void registerCommand(AbstractCommand command) {
        commandMap.put(command.getName(), command);

        command.selfRegisterWithDiscord(this.instance);
    }
}
