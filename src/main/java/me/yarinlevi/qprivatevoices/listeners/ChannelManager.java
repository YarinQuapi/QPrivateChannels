package me.yarinlevi.qprivatevoices.listeners;

import lombok.Getter;
import me.yarinlevi.qprivatevoices.database.QDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class ChannelManager extends ListenerAdapter {

    @Getter private final Map<Member, VoiceChannel> memberVoiceChannelMap = new HashMap<>();
    @Getter private final List<VoiceChannel> unclaimedChannels = new ArrayList<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        ResultSet rs = QDatabase.getInstance().get("SELECT * FROM `guildTable` WHERE `guildId`=\"" + event.getGuild().getId() + "\";");

        try {
            if (rs != null && rs.next()) {

                long categoryId = rs.getLong("categoryId");
                long joinToCreate = rs.getLong("joinToCreateId");

                if (event.getChannelJoined().getId().equalsIgnoreCase(String.valueOf(joinToCreate)) && event.getGuild().getCategoryById(categoryId) != null) {

                    String username = event.getMember().getNickname() == null ? event.getMember().getEffectiveName() : event.getMember().getNickname();

                    VoiceChannel channel = event.getGuild().getCategoryById(categoryId).createVoiceChannel(username + "'s Channel").complete();
                    Member member = event.getMember();

                    // Move the member from join channel to the new channel
                    member.getGuild().moveVoiceMember(member, channel).queue();

                    // Permission settings
                    channel.createPermissionOverride(member).setAllow(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).queue();
                    channel.createPermissionOverride(channel.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).queue();

                    memberVoiceChannelMap.put(member, channel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (memberVoiceChannelMap.containsKey(event.getMember())) {
            if (memberVoiceChannelMap.get(event.getMember()).equals(event.getChannelLeft().getJDA().getVoiceChannelById(event.getChannelLeft().getId()))) {
                memberVoiceChannelMap.remove(event.getMember());

                VoiceChannel voiceChannel = event.getChannelLeft().getJDA().getVoiceChannelById(event.getChannelLeft().getId());

                if (voiceChannel != null && voiceChannel.getMembers().size() == 0) {
                    voiceChannel.delete().queue();
                } else if (voiceChannel != null && voiceChannel.getMembers().size() != 0) {
                    unclaimedChannels.add(voiceChannel);
                }
            }
        }
    }

    /**
     * This event is when a guild user is already in 1 channel, but moves to another
     * Mimics the left and joined events to avoid duplication.
     */
    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        this.onGuildVoiceLeave(new GuildVoiceLeaveEvent(event.getJDA(), event.getResponseNumber(), event.getMember(), event.getChannelLeft()));

        this.onGuildVoiceJoin(new GuildVoiceJoinEvent(event.getJDA(), event.getResponseNumber(), event.getMember()));
    }
}
