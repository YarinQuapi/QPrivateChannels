package me.yarinlevi.qprivatevoices.commands;

import me.yarinlevi.qprivatevoices.commandhandler.AbstractCommand;
import me.yarinlevi.qprivatevoices.database.QDatabase;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author YarinQuapi
 **/
public class RegisterServer extends AbstractCommand {
    @Override
    public void run(SlashCommandEvent event, Map<String, Object> args) {
        InteractionHook hook = event.deferReply(false).complete();

        Guild guild = event.getGuild();

        try {
            ResultSet rs = QDatabase.getInstance().get("SELECT * FROM `guildTable` WHERE `guildId`=\"" + guild.getId() + "\";");

            if (rs != null && rs.next()){
                hook.editOriginalFormat("This guild is already registered.").queue();
                Logger.warning("A guild tried to register twice! (" + guild.getId() + ")");
                return;
            }
        } catch (SQLException e) {
            Logger.error("Error detected!");
            e.printStackTrace();
        }

        Role role = event.getOption("adminrole").getAsRole();
        Category category;

        if (event.getOption("categoryid") != null)
            category = guild.getCategoryById(event.getOption("categoryid").getAsLong());
        else
            category = guild.createCategory("PrivateChannels").complete();

        long joinToCreate = guild.createVoiceChannel("Join to create").complete().getIdLong();


        String sql = "INSERT INTO `guildTable` (`guildId`, `adminRole`, `categoryId`, `joinToCreateId`) VALUES (\"%s\", \"%s\", \"%s\", \"%s\")"
                .formatted(guild.getId(), role.getId(), category.getId(), joinToCreate);

        QDatabase.getInstance().insert(sql);
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public boolean getAdministrative() {
        return true;
    }

    @Override
    public void selfRegisterWithDiscord(JDA jda) {
        jda.upsertCommand(new CommandData(this.getName(), "The command used to register the server and enable private channels")
                        .addOption(OptionType.ROLE, "adminrole", "The role that will be automatically added to channels", true)
                        .addOption(OptionType.NUMBER, "categoryid", "The Id of the category you would like channels to be created at", false))
                .queue();
    }
}
