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
import com.andrei1058.discordpublicservers.customisation.Tag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;
import static com.andrei1058.discordpublicservers.BOT.log;

public class Tags extends Command {

    public Tags(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String string) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */ return;
        /** check if bot can send embed links*/
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            /** server staff commands*/
            if (string.isEmpty()){
                if (sender.hasPermission(Permission.MANAGE_ROLES)){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setThumbnail(getConfig().getLogo());
                    eb.setDescription("Categories related to this servers.\nYou can add up to 8 tags if you're premium.\n");
                    for (Tag t : Tag.values()){
                        eb.addField(t.toString(), t.getDesc(t), true);
                    }
                    eb.setFooter("Usage: 00setTags GAMING WEBSITE", sender.getUser().getAvatarUrl());
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                }
            } else {
                if (getDatabase().isGuildExists(g.getId())){
                    if (args.length > 4 && (!getDatabase().isPremium(g.getId()))){
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Sorry");
                        eb.setDescription("You can't add so many tags.\nPremium guilds can have up to 10 categories.\n Consider upgrading to premium for more features.");
                        eb.setColor(getConfig().getColor());
                        c.sendMessage(eb.build()).queue();
                        return;
                    } else if (args.length > 10){
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Sorry");
                        eb.setDescription("You can't add so many tags.");
                        eb.setColor(getConfig().getColor());
                        c.sendMessage(eb.build()).queue();
                    }
                    for (String  s : args){
                        try {
                            Tag.valueOf(s.toUpperCase());
                        } catch (Exception e){
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setDescription("Invalid tag: `"+s+"`");
                            eb.setColor(getConfig().getColor());
                            c.sendMessage(eb.build()).queue();
                            EmbedBuilder eb2 = new EmbedBuilder();
                            eb2.setThumbnail(getConfig().getLogo());
                            eb2.setDescription("Categories related to this servers.\nYou can add up to 8 tags if you're premium.\n\n");
                            for (Tag t : Tag.values()){
                                eb2.addField(t.toString(), t.getDesc(t), true);
                            }
                            eb2.setFooter("Usage: 00setTags GAMING WEBSITE", sender.getUser().getAvatarUrl());
                            eb2.setColor(getConfig().getColor());
                            c.sendMessage(eb2.build()).queue();
                            return;
                        }
                    }
                    getDatabase().updateTags(g.getId(), string);
                    getDatabase().updateLastTime(g.getId());
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription("Tags saved: `"+string+"`");
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                    log("Tags update on: "+g.getName()+": "+string);
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription("Your server isn't in our database.");
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                }
            }
        } else {
            /** server staff commands*/
            if (sender.hasPermission(Permission.MANAGE_ROLES)){
                if (string.isEmpty()){
                    c.sendMessage("Categories related to this servers.\nYou can add up to 8 tags if you're premium.\n");
                } else {
                    if (string.isEmpty()){
                        for (String  s : args){
                            try {
                                Tag.valueOf(s);
                            } catch (Exception e){
                                c.sendMessage("Categories related to this servers.\nYou can add up to 8 tags if you're premium.\n\n").queue();
                                for (Tag t : Tag.values()){
                                    c.sendMessage(t.toString()).queue();
                                }
                                c.sendMessage("Usage: `00setTags GAMING WEBSITE`").queue();
                                return;
                            }
                        }
                        return;
                    }
                    if (getDatabase().isGuildExists(g.getId())){
                        if (args.length > 4 && (!getDatabase().isPremium(g.getId()))){
                            c.sendMessage("Sorry").queue();
                            c.sendMessage("You can't add so many tags.\nPremium guilds can have up to 10 categories.\n Consider upgrading to premium for more features.").queue();
                            return;
                        } else if (args.length > 10){
                            c.sendMessage("Sorry").queue();
                            c.sendMessage("You can't add so many tags.").queue();
                        }
                        for (String  s : args) {
                            try {
                                Tag.valueOf(s.toUpperCase());
                            } catch (Exception e) {
                                c.sendMessage("Invalid tag: `"+s+"`").queue();
                                c.sendMessage("Categories related to this servers.\nYou can add up to 8 tags if you're premium.\n\n").queue();
                                for (Tag t : Tag.values()){
                                    c.sendMessage(t.toString()).queue();
                                }
                                c.sendMessage("Usage: `00setTags GAMING WEBSITE`").queue();
                                return;
                            }
                        }
                        c.sendMessage("Tags set: `"+string+"`").queue();
                        getDatabase().updateTags(g.getId(), string);
                        getDatabase().updateLastTime(g.getId());
                        log("Tags update on: "+g.getName()+": "+string);
                    } else {
                        c.sendMessage("Your server isn't in our database.").queue();
                    }
                }
            }
        }
    }
}
