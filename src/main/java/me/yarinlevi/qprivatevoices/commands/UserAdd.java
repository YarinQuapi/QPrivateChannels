package me.yarinlevi.qprivatevoices.commands;

import me.yarinlevi.qprivatevoices.commandhandler.AbstractCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Map;

/**
 * @author YarinQuapi
 * A command to add users to a private channel
 **/
public class UserAdd extends AbstractCommand {
    @Override
    public void run(Map<String, Object> args) {

    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public boolean getAdministrative() {
        return false;
    }

    @Override
    public void selfRegisterWithDiscord(JDA jda) {
        jda.upsertCommand(new CommandData(this.getName(), "Add a user to the channel")
                .addOptions(new OptionData(OptionType.USER, "user", "User to add to the channel")
                        .setRequired(true))).queue();
    }
}
