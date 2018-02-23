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

package com.andrei1058.discordpublicservers.commands.server;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;
import static com.andrei1058.discordpublicservers.BOT.log;

public class Lang extends Command {

    public Lang(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String string) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        /** check if bot can send embed links*/
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            /** server staff commands*/
            if (string.isEmpty()) {
                if (sender.hasPermission(Permission.MANAGE_ROLES)) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setThumbnail(getConfig().getLogo());
                    eb.setDescription("Languages spoken on this server.\nYou can add up to 8 languages if you're premium.\n");
                    for (com.andrei1058.discordpublicservers.customisation.Lang t : com.andrei1058.discordpublicservers.customisation.Lang.values()) {
                        eb.addField(t.toString(), t.getDesc(t), true);
                    }
                    eb.setFooter("Usage: 00setLang ENGLISH ROMANIAN", sender.getUser().getAvatarUrl());
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                }
            } else {
                if (getDatabase().isGuildExists(g.getId())) {
                    if (args.length > 4 && (!getDatabase().isPremium(g.getId()))) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Sorry");
                        eb.setDescription("You can't add so many languages.\nPremium guilds can have up to 8 languages.\nConsider upgrading to premium for more features.");
                        eb.setColor(getConfig().getColor());
                        c.sendMessage(eb.build()).queue();
                        return;
                    } else if (args.length > 10) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Sorry");
                        eb.setDescription("You can't add so many languages.");
                        eb.setColor(getConfig().getColor());
                        c.sendMessage(eb.build()).queue();
                    }
                    for (String s : args) {
                        try {
                            com.andrei1058.discordpublicservers.customisation.Lang.valueOf(s.toUpperCase());
                        } catch (Exception e) {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setDescription("Invalid language: `" + s + "`");
                            eb.setColor(getConfig().getColor());
                            c.sendMessage(eb.build()).queue();
                            EmbedBuilder eb2 = new EmbedBuilder();
                            eb2.setThumbnail(getConfig().getLogo());
                            eb2.setDescription("Languages spoken on this server.\nYou can add up to 8 languages if you're premium.\n");
                            for (com.andrei1058.discordpublicservers.customisation.Lang t : com.andrei1058.discordpublicservers.customisation.Lang.values()) {
                                eb2.addField(t.toString(), t.getDesc(t), true);
                            }
                            eb2.setFooter("Usage: 00setLang ENGLISH ROMANIAN", sender.getUser().getAvatarUrl());
                            eb2.setColor(getConfig().getColor());
                            c.sendMessage(eb2.build()).queue();
                            return;
                        }
                    }
                    getDatabase().updateLanguage(g.getId(), string);
                    getDatabase().updateLastTime(g.getId());
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription("Language saved: `" + string + "`");
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                    log("Language update on: " + g.getName() + ": " + string);
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription("Your server isn't in our database.");
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                }
            }
        } else {
            /** server staff commands*/
            if (sender.hasPermission(Permission.MANAGE_ROLES)) {
                if (string.isEmpty()) {
                    for (String s : args) {
                        try {
                            com.andrei1058.discordpublicservers.customisation.Lang.valueOf(s);
                        } catch (Exception e) {
                            c.sendMessage("Languages spoken on this server.\nYou can add up to 8 languages if you're premium.\n").queue();
                            for (com.andrei1058.discordpublicservers.customisation.Lang t : com.andrei1058.discordpublicservers.customisation.Lang.values()) {
                                c.sendMessage(t.toString()).queue();
                            }
                            c.sendMessage("Usage: `00setLang ENGLISH ROMANIAN`").queue();
                            return;
                        }
                    }
                    return;
                }
                if (getDatabase().isGuildExists(g.getId())) {
                    if (args.length > 4 && (!getDatabase().isPremium(g.getId()))) {
                        c.sendMessage("Sorry").queue();
                        c.sendMessage("You can't add so many languages.\nPremium guilds can have up to 8 languages.\nConsider upgrading to premium for more features.").queue();
                        return;
                    } else if (args.length > 10) {
                        c.sendMessage("Sorry").queue();
                        c.sendMessage("You can't add so many languages.").queue();
                    }
                    for (String s : args) {
                        try {
                            com.andrei1058.discordpublicservers.customisation.Lang.valueOf(s.toUpperCase());
                        } catch (Exception e) {
                            c.sendMessage("Invalid language: `" + s + "`").queue();
                            c.sendMessage("Languages spoken on this server.\nYou can add up to 8 languages if you're premium.\n").queue();
                            for (com.andrei1058.discordpublicservers.customisation.Lang t : com.andrei1058.discordpublicservers.customisation.Lang.values()) {
                                c.sendMessage(t.toString()).queue();
                            }
                            c.sendMessage("Usage: `00setLang ENGLISH ROMANIAN`").queue();
                            return;
                        }
                    }
                    c.sendMessage("Language set: `" + string + "`").queue();
                    getDatabase().updateLanguage(g.getId(), string);
                    getDatabase().updateLastTime(g.getId());
                    log("Language update on: " + g.getName() + ": " + string);
                } else {
                    c.sendMessage("Your server isn't in our database.").queue();
                }
            }
        }
    }
}
