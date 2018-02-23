/*
 * MIT License
 *
 * Copyright (c) 2018 Andrei Dascălu
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

package com.andrei1058.discordpublicservers.misc;

import com.andrei1058.discordpublicservers.commands.Help;
import com.andrei1058.discordpublicservers.commands.member.Report;
import com.andrei1058.discordpublicservers.commands.member.Vote;
import com.andrei1058.discordpublicservers.commands.server.*;
import com.andrei1058.discordpublicservers.commands.service.*;
import com.andrei1058.discordpublicservers.customisation.Lang;
import com.andrei1058.discordpublicservers.customisation.Messages;
import com.andrei1058.discordpublicservers.customisation.Tag;
import com.andrei1058.discordpublicservers.listeners.CollectData;
import com.andrei1058.discordpublicservers.listeners.Message;
import com.andrei1058.discordpublicservers.listeners.Ready;
import com.andrei1058.discordpublicservers.listeners.Shutdown;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Interval;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class Misc {

    private static List<Long> announce = new ArrayList<>();

    public static void checkPremiumExpire(){
        for (Long l : getDatabase().getPremiumServers()){
            DateTime d1 = new DateTime(System.currentTimeMillis()), d2 = new DateTime(getDatabase().getPremiumExpire(String.valueOf(l)).getTime());
            int d = Days.daysBetween(d1, d2).getDays();
            if (d == 7 || d == 3 || d == 1){
                if (!announce.contains(l)){
                    announce.add(l);
                    Guild g = getBot().getGuildById(l);
                    if (g != null) {
                        Messages.send(g, g.getOwner().getUser(), Messages.Message.PREMIUM_EXPIRE_SOON);
                    }
                }
            } else {
                if (announce.contains(l)){
                    announce.remove(l);
                }
            }
            if (d == 0){
                int h = Hours.hoursBetween(d1, d2).getHours();
                if (h <= 0){
                    Guild g = getBot().getGuildById(l);
                    if (g != null) {
                        Messages.send(g, g.getOwner().getUser(), Messages.Message.PREMIUM_EXPIRED);
                    }
                    getDatabase().removePremium(String.valueOf(l));
                }
            } else if (d < 0){
                Guild g = getBot().getGuildById(l);
                if (g != null) {
                    Messages.send(g, g.getOwner().getUser(), Messages.Message.PREMIUM_EXPIRED);
                }
                getDatabase().removePremium(String.valueOf(l));
            }
        }
    }

    public static String createInviteLink(Guild g) {
        String i = "";
        if (g.getDefaultChannel() != null) {
            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.MANAGE_SERVER)) {
                for (Invite i2 : g.getInvites().complete()) {
                    if (i2.getInviter() == getBot().getSelfUser()) {
                        if (i2.isTemporary()) {
                            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                                return g.getDefaultChannel().createInvite().setMaxAge(0).complete().getURL();
                            }
                        } else {
                            return i2.getURL();
                        }
                    }
                }
            }
        }
        for (TextChannel tc : g.getTextChannels()) {
            if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.MANAGE_SERVER)) {
                for (Invite i2 : g.getInvites().complete()) {
                    if (i2.getInviter() == getBot().getSelfUser()) {
                        if (i2.isTemporary()) {
                            if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                                return g.getDefaultChannel().createInvite().setMaxAge(0).complete().getURL();
                            }
                        } else {
                            return i2.getURL();
                        }
                    }
                }
            }
        }
        if (g.getDefaultChannel() != null) {
            if (PermissionUtil.checkPermission(g.getDefaultChannel(), g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                return g.getDefaultChannel().createInvite().setMaxAge(0).complete().getURL();
            }
        }
        for (TextChannel tc : g.getTextChannels()) {
            if (PermissionUtil.checkPermission(tc, g.getSelfMember(), Permission.CREATE_INSTANT_INVITE)) {
                return g.getDefaultChannel().createInvite().setMaxAge(0).complete().getURL();
            }
        }
        Messages.send(g, g.getOwner().getUser(), Messages.Message.CANT_CREATE_INVITE_LINK);
        return i;
    }

    public static void guildsRefresh() {
        for (Guild g : getBot().getGuilds()) {
            if (getDatabase().isGuildBanned(g.getId())){
                Messages.send(g, g.getOwner().getUser(), Messages.Message.CANT_SCAN_GUILD_BANED);
                g.leave().complete();
                continue;
            } else if (getDatabase().isUserBanned(g.getOwner().getUser().getId())){
                Messages.send(g, g.getOwner().getUser(), Messages.Message.CANT_SCAN_USER_BANNED);
                g.leave().complete();
                continue;
            }
            String i = Misc.createInviteLink(g);
            if (i.isEmpty()) continue;
            if (getDatabase().isGuildExists(g.getId())) {
                if (!getDatabase().isShown(g.getId())) {
                    getDatabase().showGuild(g.getId());
                    Messages.send(g, g.getOwner().getUser(), Messages.Message.GUILD_RE_ADDED);
                }
                getDatabase().updateGuildData(g.getIdLong(), g.getName(), g.getMembers().stream()
                                .filter(m -> !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, g.getMembers().size(),
                        g.getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, g.getOwner().getUser().getIdLong(), g.getOwner().getEffectiveName(),
                        i, g.getIconUrl());
            } else {
                getDatabase().addNewServer(g.getIdLong(), g.getName(), g.getMembers().stream().filter(m ->
                                !(m.getOnlineStatus() == OnlineStatus.OFFLINE || m.getOnlineStatus() == OnlineStatus.INVISIBLE)).toArray().length, g.getMembers().size(),
                        g.getMembers().stream().filter(m -> m.getUser().isBot()).toArray().length, g.getOwner().getUser().getIdLong(), g.getOwner().getEffectiveName(),
                        i, g.getIconUrl(), Tag.GAMING.toString(), Lang.NONE.toString());
                Messages.send(g, g.getOwner().getUser(), Messages.Message.NEW_GUILD_ADDED);
            }
        }
    }

    public static TextChannel getEmbed(Guild g){
        for (TextChannel c : g.getTextChannels()){
            if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)
                    && PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)){
                return c;
            }
        }
        return null;
    }

    public static TextChannel getText(Guild g){
        for (TextChannel c : g.getTextChannels()){
            if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)){
                return c;
            }
        }
        return null;
    }

    public static void createStartScript(){
        File f = new File("start.sh");
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    PrintWriter pw = new PrintWriter("start.sh", "UTF-8");
                    pw.println("java -jar discordpublicservers-1.0-SNAPSHOT-jar-with-dependencies.jar");
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isLong(String s){
        try {
            Long.parseLong(s);
        } catch (Exception ex){
            return false;
        }
        return true;
    }

    public static boolean isInt(String s){
        try {
            Integer.parseInt(s);
        } catch (Exception ex){
            return false;
        }
        return true;
    }

    public static void registerCommands(){
        new Help("help");
        new Description("setdesc");
        new Tags("settags");
        new com.andrei1058.discordpublicservers.commands.server.Lang("setlang");
        new Bump("bump");
        new Vote("vote");
        new Feedback("feedback");
        new Votes("votes");
        new Stop("stop");
        new Restart("restart");
        new BanUser("banuser");
        new UnBanUser("unbanuser");
        new BanServer("banserver");
        new UnBanServer("unbanserver");
        new MakePremium("makepremium");
        new DelPremium("delpremium");
        new Stats("stats");
        new SetStatus("setstatus");
        new Report("report");
    }

    public static void registerEvents(){
        getBot().addEventListener(new Ready());
        getBot().addEventListener(new Message());
        getBot().addEventListener(new Shutdown());
        getBot().addEventListener(new CollectData());
    }
}
