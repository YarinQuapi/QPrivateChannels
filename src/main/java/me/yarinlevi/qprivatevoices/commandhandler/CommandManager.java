package me.yarinlevi.qprivatevoices.commandhandler;

import me.yarinlevi.qprivatevoices.commands.ClaimChannel;
import me.yarinlevi.qprivatevoices.commands.UserAdd;
import me.yarinlevi.qprivatevoices.commands.UserRemove;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commandMap = new HashMap<>();

    private JDA instance;

    public void complete(JDA jda) {
        this.instance = jda;

        this.registerCommand(new UserAdd());
        this.registerCommand(new UserRemove());
        this.registerCommand(new ClaimChannel());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Map<String, Object> commandArguments = new HashMap<>();

        commandArguments.put("channel", event.getChannel());
        commandArguments.put("guild", event.getGuild());
        commandArguments.put("member", event.getMember());
        commandArguments.put("pure", event.getCommandPath());
        commandArguments.put("options", new ArrayList<>(event.getOptions()));

        commandMap.get(event.getName()).run(event, commandArguments);
    }

    protected void registerCommand(AbstractCommand command) {
        commandMap.put(command.getName(), command);

        command.selfRegisterWithDiscord(this.instance);

        Logger.info("Sent command '" + command.getName() + "' for registration");
    }
}
