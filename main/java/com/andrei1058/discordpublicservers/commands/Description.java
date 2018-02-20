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

package com.andrei1058.discordpublicservers.commands;

import com.andrei1058.discordpublicservers.BOT;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

import static com.andrei1058.discordpublicservers.BOT.*;

public class Description extends Command {

    public Description(String name) {
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
                    eb.setDescription("Set a description to be displayed on our website.");
                    eb.setColor(Color.PINK);
                    c.sendMessage(eb.build()).queue();
                }
            } else {
                if (getDatabase().isGuildExists(g.getId())){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.addField("Description set:", string, true);
                    eb.setColor(Color.PINK);
                    c.sendMessage(eb.build()).queue();
                    getDatabase().updateDesc(g.getId(), string);
                    getDatabase().updateLastTime(g.getId());
                    log("Description update on: "+g.getName()+": "+string);
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription("Your server isn't in our database.");
                    eb.setColor(Color.PINK);
                    c.sendMessage(eb.build()).queue();
                }
            }
        } else {
            /** server staff commands*/
            if (sender.hasPermission(Permission.MANAGE_ROLES)){
                if (string.isEmpty()){
                    c.sendMessage("Set a description to be displayed on our website.");
                } else {
                    if (getDatabase().isGuildExists(g.getId())){
                        c.sendMessage("Description set: `"+string+"`").queue();
                        getDatabase().updateDesc(g.getId(), string);
                        getDatabase().updateLastTime(g.getId());
                        log("Description update on: "+g.getName()+": "+string);
                    } else {
                        c.sendMessage("Your server isn't in our database.").queue();
                    }
                }
            }
        }
    }
}
