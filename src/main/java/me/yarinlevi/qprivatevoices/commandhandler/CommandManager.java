package me.yarinlevi.qprivatevoices.commandhandler;

import me.yarinlevi.qprivatevoices.commands.ClaimChannel;
import me.yarinlevi.qprivatevoices.commands.RegisterServer;
import me.yarinlevi.qprivatevoices.commands.UserAdd;
import me.yarinlevi.qprivatevoices.commands.UserRemove;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
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

        this.registerCommand(new RegisterServer());
        this.registerCommand(new UserAdd());
        this.registerCommand(new UserRemove());
        this.registerCommand(new ClaimChannel());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null) {
            event.reply("Achievement unlocked! How did we get here? (no, really, this should be executed in a server.)").queue();
            return;
        }

        Map<String, Object> commandArguments = new HashMap<>();

        commandArguments.put("channel", event.getChannel());
        commandArguments.put("guild", event.getGuild());
        commandArguments.put("member", event.getMember());
        commandArguments.put("pure", event.getCommandPath());
        commandArguments.put("options", new ArrayList<>(event.getOptions()));

        Command command = commandMap.get(event.getName());

        if (command.getAdministrative()) {
            if (event.getMember() != null && event.getMember().getPermissions().contains(Permission.ADMINISTRATOR))
                command.run(event, commandArguments);
        } else {
            command.run(event, commandArguments);
        }
    }

    protected void registerCommand(AbstractCommand command) {
        commandMap.put(command.getName(), command);

        command.selfRegisterWithDiscord(this.instance);

        Logger.info("Sent command '" + command.getName() + "' for registration");
    }
}
