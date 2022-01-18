package me.yarinlevi.qprivatevoices.commands;

import me.yarinlevi.qprivatevoices.QPrivateVoices;
import me.yarinlevi.qprivatevoices.commandhandler.AbstractCommand;
import me.yarinlevi.qprivatevoices.listeners.ChannelManager;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;
import java.util.Map;

/**
 * @author YarinQuapi
 **/
public class UserRemove extends AbstractCommand {
    @Override
    public void run(SlashCommandEvent event, Map<String, Object> args) {
        InteractionHook hook = event.deferReply(true).complete();

        Member member = (Member) args.get("member");

        List<OptionMapping> options = (List<OptionMapping>) args.get("options");

        Member mentionedMember = options.get(0).getAsMember();

        if (member.getVoiceState() != null && member.getVoiceState().inAudioChannel()) {
            VoiceChannel voiceChannel = member.getVoiceState().getJDA().getVoiceChannelById(member.getVoiceState().getId());

            ChannelManager cm = QPrivateVoices.getInstance().getChannelManager();

            if (cm.getMemberVoiceChannelMap().containsKey(member)) {
                if (cm.getMemberVoiceChannelMap().get(member).equals(voiceChannel)) {
                    if (mentionedMember != null) {
                        if (voiceChannel.getMemberPermissionOverrides().stream().anyMatch(x -> x.getMember().getIdLong() == mentionedMember.getIdLong())) {
                            voiceChannel.createPermissionOverride(mentionedMember).setDeny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).queue();
                            hook.editOriginalFormat("Denied user access to this channel").queue();
                        } else {
                            hook.editOriginalFormat("This user is not permitted to enter this channel").queue();
                        }
                    } else {
                        Logger.warning("Mentioned guild member was not found at guild: " + member.getGuild().getId() + ", command by user: " + member.getEffectiveName() + "#" + member.getUser().getDiscriminator() + " : " + member.getId());
                        hook.editOriginalFormat("Mentioned member was not found, please try again.").queue();
                    }
                } else {
                    hook.editOriginalFormat("Hey, something went very very very wrong if you see this message, please report it immediately. it should really never ever appear.").queue();
                    Logger.error("Error code: E001 detected. Contact the developer immediately if self hosted!");
                }
            } else {
                hook.editOriginalFormat("You are not the owner of the channel.").queue();
            }
        } else {
            hook.editOriginal("You are not connected to a voice channel.").queue();
        }
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public boolean getAdministrative() {
        return false;
    }

    @Override
    public void selfRegisterWithDiscord(JDA jda) {
        jda.upsertCommand(new CommandData(this.getName(), "Remove a user from the channel")
                        .addOption(OptionType.USER, "user", "The user to remove", true))
                .queue();
    }
}
