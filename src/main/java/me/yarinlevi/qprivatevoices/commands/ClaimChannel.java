package me.yarinlevi.qprivatevoices.commands;

import me.yarinlevi.qprivatevoices.QPrivateVoices;
import me.yarinlevi.qprivatevoices.commandhandler.AbstractCommand;
import me.yarinlevi.qprivatevoices.listeners.ChannelManager;
import me.yarinlevi.qprivatevoices.utilities.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Map;

/**
 * @author YarinQuapi
 **/
public class ClaimChannel extends AbstractCommand {
    @Override
    public void run(SlashCommandEvent event, Map<String, Object> args) {
        InteractionHook hook = event.deferReply(true).complete();

        Member member = (Member) args.get("member");

        if (member.getVoiceState() != null && member.getVoiceState().inAudioChannel()) {
            VoiceChannel voiceChannel = member.getVoiceState().getJDA().getVoiceChannelById(member.getVoiceState().getId());

            ChannelManager cm = QPrivateVoices.getInstance().getChannelManager();

            if (cm.getUnclaimedChannels().contains(voiceChannel)) {
                cm.getMemberVoiceChannelMap().put(member, voiceChannel);
                cm.getUnclaimedChannels().remove(voiceChannel);

                hook.editOriginalFormat("You have claimed the channel.").queue();
            } else {
                hook.editOriginalFormat("You are not in an unclaimed channel.").queue();
            }
        } else {
            hook.editOriginalFormat("You are not connected to a voice channel.").queue();
        }
    }

    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public boolean getAdministrative() {
        return false;
    }

    @Override
    public void selfRegisterWithDiscord(JDA jda) {
        jda.upsertCommand(new CommandData("claim", "Claim an unclaimed channel")).queue();
    }
}
