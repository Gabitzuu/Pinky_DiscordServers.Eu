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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.joda.time.*;

import java.util.concurrent.TimeUnit;

import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class Bump extends Command {

    public Bump(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!sender.hasPermission(Permission.MANAGE_ROLES)) return;
        /** check if bot can send embed links*/
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            if (getDatabase().isGuildExists(g.getId())) {
                Duration d = new Duration(getDatabase().getLastBump(g.getId()).getTime() + TimeUnit.DAYS.toDays(1), System.currentTimeMillis());
                if (d.toStandardDays().isLessThan(Days.ONE)){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Sorry");
                    DateTime date = new DateTime(getDatabase().getLastBump(g.getId()).getTime()).plusDays(1), date2 = new DateTime(System.currentTimeMillis());
                    eb.setDescription("You can bump a server once a day.\nConsider donating for premium features if you want your server to be on top.\n" +
                            "Next bump in: "+ Hours.hoursBetween(date2, date).getHours()+" hours");
                    eb.setColor(getConfig().getColor());
                    c.sendMessage(eb.build()).queue();
                    return;
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription("Your server was bumped!");
                eb.setColor(getConfig().getColor());
                c.sendMessage(eb.build()).queue();
                getDatabase().bumpGuild(g.getId());
            } else {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription("Your server isn't in our database.");
                eb.setColor(getConfig().getColor());
                c.sendMessage(eb.build()).queue();
            }
        } else {
            if (getDatabase().isGuildExists(g.getId())) {
                Duration d = new Duration(getDatabase().getLastBump(g.getId()).getTime(), System.currentTimeMillis());
                if (d.toStandardDays().isLessThan(Days.ONE)){
                    c.sendMessage("Sorry\nYou can bump a server once a day.\nConsider donating for premium features if you want your server to be on top.").queue();
                    return;
                }
                c.sendMessage("Your server was bumped!").queue();
                getDatabase().bumpGuild(g.getId());
            } else {
                c.sendMessage("Your server isn't in our database.").queue();
            }
        }
    }
}
