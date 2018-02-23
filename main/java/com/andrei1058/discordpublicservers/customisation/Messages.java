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

package com.andrei1058.discordpublicservers.customisation;

import com.andrei1058.discordpublicservers.misc.Misc;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import static com.andrei1058.discordpublicservers.BOT.*;

public class Messages {

    public enum Message {
        NEW_GUILD_ADDED, GUILD_RE_ADDED, CANT_SCAN_GUILD_BANED, CANT_SCAN_USER_BANNED, CANT_CREATE_INVITE_LINK, BOUGHT_PREMIUM, PREMIUM_EXPIRE_SOON, PREMIUM_EXPIRED
    }

    public static void send(Guild g, User u, Message m){
        switch (m){
            default:
            case NEW_GUILD_ADDED:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Your server was added!");
                    b.addField("00help", "Main command.", true);
                    Misc.getEmbed(g).sendMessage(b.build()).queue();
                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage(":blobaww:\nYour server was added on **DiscordServers.Eu**\nMain command: `00help`").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Your server `"+g.getName()+"` was added!");
                    b.addField("00help", "Main command.", true);
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                }
                break;
            case GUILD_RE_ADDED:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Your server was added!");
                    b.addField("00help", "Main command.", true);
                    Misc.getEmbed(g).sendMessage(b.build()).queue();
                    Misc.getEmbed(g).sendMessage(u.getAsMention()).complete();

                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage(":blobaww:\nYour server was added on **DiscordServers.Eu**\nMain command: `00help`").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Your server `"+g.getName()+"` was added!");
                    b.addField("00help", "Main command.", true);
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                }
                break;
            case CANT_SCAN_GUILD_BANED:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't stay on this server because this server is banned.\nReason: "+getDatabase().getGuildBanReason(g.getId()));
                    Misc.getEmbed(g).sendMessage(b.build()).queue();

                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("I can't stay on this server because this server is banned.\nReason: "+getDatabase().getGuildBanReason(g.getId())+"").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't stay on `"+g.getName()+"` because it is banned. Reason: "+getDatabase().getGuildBanReason(g.getId()));
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                }
                break;
            case CANT_SCAN_USER_BANNED:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't stay on this server because the owner is banned.\nReason: "+getDatabase().getGuildBanReason(g.getId())+"");
                    Misc.getEmbed(g).sendMessage(b.build()).queue();

                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("I can't stay on this server because the owner is banned.\nReason: `"+getDatabase().getGuildBanReason(g.getId())+"`").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't stay on `"+g.getName()+"` because you're banned.\nReason: "+getDatabase().getGuildBanReason(g.getId())+"");
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                }
                break;
            case CANT_CREATE_INVITE_LINK:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't create invites on this server.\nPlease create a new role for me with those perms.\nManage Channel is optional and it is required to prevent audit logs spam.");
                    b.setImage("https://i.imgur.com/H93NxI6.png");
                    Misc.getEmbed(g).sendMessage(b.build()).queue();

                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("I can't create invites on this server.\nPlease create a new role for me with those permissions." +
                            "\nManage Channel is optional and it is required to prevent audit logs spam.\nhttps://i.imgur.com/H93NxI6.png").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("I can't create invites on `"+g.getName()+"`.\nPlease create a new role for me with those perms.\nManage Channel is optional and it is required to prevent audit logs spam.");
                    b.setImage("https://i.imgur.com/H93NxI6.png");
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case BOUGHT_PREMIUM:
                DateTime d = new DateTime(getDatabase().getPremiumExpire(g.getId()).getTime());
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Thanks for donating for premium :heart:\nIt will expire on "+d.monthOfYear().getAsShortText()+" "+d.dayOfMonth().get()+" "+d.year().get()+" at "+d.hourOfDay().get()+" o'clock.");
                    Misc.getEmbed(g).sendMessage(b.build()).queue();
                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("Thanks for donating for premium :heart:\nIt will expire on "+d.monthOfYear().getAsShortText()+" "+d.dayOfMonth().get()+" "+d.year().get()+" at "+d.hourOfDay().get()+" o'clock.").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Thanks for donating for premium :heart:\nPremium for "+g.getName()+" will expire on "+d.monthOfYear().getAsShortText()+" "+d.dayOfMonth().get()+" "+d.year().get()+" at "+d.hourOfDay().get()+" o'clock.");
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case PREMIUM_EXPIRE_SOON:
                DateTime expire = new DateTime(getDatabase().getPremiumExpire(g.getId()).getTime());
                int days = Days.daysBetween(new DateTime(System.currentTimeMillis()), expire).getDays();
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Premium will expire in "+days+" days!\nPlease consider donating again :slight_slime:");
                    Misc.getEmbed(g).sendMessage(b.build()).queue();
                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("Premium will expire in "+days+" days!\nPlease consider donating again :slight_slime:").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Premium for "+g.getName()+" will expire in "+days+"\nPlease consider donating again :slight_slime:");
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case PREMIUM_EXPIRED:
                if (Misc.getEmbed(g) != null){
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Premium features have expired :frowning:");
                    Misc.getEmbed(g).sendMessage(b.build()).queue();
                } else if (Misc.getText(g) != null){
                    Misc.getText(g).sendMessage("Premium features have expired :frowning:\nPlease consider donating again :slight_slime:").queue();
                }
                try {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setColor(getConfig().getColor());
                    b.setTitle("DiscordServers.Eu", getConfig().getLogo());
                    b.setThumbnail(getConfig().getLogo());
                    b.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                    b.setDescription("Premium features for "+g.getName()+" have expired\nPlease consider donating again :slight_slime:");
                    u.openPrivateChannel().complete().sendMessage(b.build()).queue();
                } catch (Exception e){
                    log(e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }
}
