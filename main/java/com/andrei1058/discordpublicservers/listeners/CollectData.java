/*
 * MIT License
 *
 * Copyright (c) 2018 Andrei Dascalu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.andrei1058.discordpublicservers.listeners;

import com.andrei1058.discordpublicservers.Misc;
import com.andrei1058.discordpublicservers.customisation.Lang;
import com.andrei1058.discordpublicservers.customisation.Messages;
import com.andrei1058.discordpublicservers.customisation.Tag;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class CollectData extends ListenerAdapter {

    /** bot join */
    @Override
    public void onGuildJoin(GuildJoinEvent e){
        if (getDatabase().isGuildBanned(e.getGuild().getId())){
            Messages.send(e.getGuild(), e.getGuild().getOwner().getUser(), Messages.Message.CANT_SCAN_GUILD_BANED);
            e.getGuild().leave();
            return;
        } else if (getDatabase().isUserBanned(e.getGuild().getOwner().getUser().getId())){
            Messages.send(e.getGuild(), e.getGuild().getOwner().getUser(), Messages.Message.CANT_SCAN_USER_BANNED);
            e.getGuild().leave();
            return;
        }
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            if (!getDatabase().isShown(e.getGuild().getId())){
                getDatabase().showGuild(e.getGuild().getId());
                Messages.send(e.getGuild(), e.getGuild().getOwner().getUser(), Messages.Message.GUILD_RE_ADDED);
            }
            String invite = Misc.createInviteLink(e.getGuild());
            if (invite.isEmpty()){
                e.getGuild().leave().complete();
                return;
            }
            getDatabase().updateGuildData(e.getGuild().getIdLong(), e.getGuild().getName(), e.getGuild().getMembers().stream()
                    .filter(m -> !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, e.getGuild().getMembers().size(),
                    e.getGuild().getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, e.getGuild().getOwner().getUser().getIdLong(), e.getGuild().getOwner().getEffectiveName(),
                    invite, e.getGuild().getIconUrl());
        } else {
            String invite = Misc.createInviteLink(e.getGuild());
            if (invite.isEmpty()){
                e.getGuild().leave().complete();
                return;
            }
            getDatabase().addNewServer(e.getGuild().getIdLong(), e.getGuild().getName(), e.getGuild().getMembers().stream().filter(m ->
            !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, e.getGuild().getMembers().size(),
                    e.getGuild().getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, e.getGuild().getOwner().getUser().getIdLong(), e.getGuild().getOwner().getEffectiveName(),
                    invite, e.getGuild().getIconUrl(), Tag.GAMING.toString(), Lang.NONE.toString());
            Messages.send(e.getGuild(), e.getGuild().getOwner().getUser(), Messages.Message.NEW_GUILD_ADDED);
        }
    }
    /** bot leave*/
    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            if (getDatabase().isShown(e.getGuild().getId())){
                getDatabase().hideGuild(e.getGuild().getId());
            }
        }
    }

    /** on_users && tot_users */
    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent e) {
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateMembersCount(e.getGuild().getId(), e.getGuild().getMembers().stream().filter(m ->
                    !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, e.getGuild().getMembers().size());
            //todo update date
        }
    }

    /** on_users && tot_users && bot_amount */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateMembersCount(e.getGuild().getId(), e.getGuild().getMembers().stream().filter(m ->
                    !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, e.getGuild().getMembers().size());
            getDatabase().updateBotsCount(e.getGuild().getId(), e.getGuild().getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length);
            getDatabase().updateLastTime(e.getGuild().getId());
        }
    }

    /** on_users && tot_users && bot_amount */
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateMembersCount(e.getGuild().getId(), e.getGuild().getMembers().stream().filter(m ->
                    !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, e.getGuild().getMembers().size());
            getDatabase().updateBotsCount(e.getGuild().getId(), e.getGuild().getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length);
            getDatabase().updateLastTime(e.getGuild().getId());
        }
    }

    /** server_name */
    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateGuildName(e.getGuild().getId(), e.getGuild().getName());
            getDatabase().updateLastTime(e.getGuild().getId());
        }
    }

    /** owner_id && owner_name */
    public void onGuildUpdateOwner(GuildUpdateOwnerEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateGuildOwner(e.getGuild().getId(), e.getGuild().getOwner().getUser().getName(), e.getGuild().getOwner().getUser().getIdLong());
            getDatabase().updateLastTime(e.getGuild().getId());
        }
    }

    /** server_icon */
    public void onGuildUpdateIcon(GuildUpdateIconEvent e){
        if (getDatabase().isGuildExists(e.getGuild().getId())){
            getDatabase().updateGuildIcon(e.getGuild().getId(), e.getGuild().getIconUrl());
            getDatabase().updateLastTime(e.getGuild().getId());
        }
    }
}
