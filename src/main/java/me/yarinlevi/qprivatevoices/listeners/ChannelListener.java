package me.yarinlevi.qprivatevoices.listeners;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class ChannelListener extends ListenerAdapter {

    @Getter private final Map<Member, VoiceChannel> memberVoiceChannelMap = new HashMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        System.out.println("detected");

        event.getGuild().getDefaultChannel().sendMessage("test").queue();
        System.out.println("detected");

        if (event.getChannelJoined().getName().equalsIgnoreCase("join to create")) {
            event.getGuild().getDefaultChannel().sendMessage("test").queue();

            VoiceChannel channel = event.getGuild().createVoiceChannel(event.getMember().getNickname() + "'s Channel").complete();
            event.getGuild().getDefaultChannel().sendMessage("test").queue();

            Member member = event.getMember();
            event.getGuild().getDefaultChannel().sendMessage("test").queue();

            member.getGuild().moveVoiceMember(member, channel).queue();
            event.getGuild().getDefaultChannel().sendMessage("test").queue();

            memberVoiceChannelMap.put(member, channel);
            event.getGuild().getDefaultChannel().sendMessage("test").queue();
        }
    }
}
