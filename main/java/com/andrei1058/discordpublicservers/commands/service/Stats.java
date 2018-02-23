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

package com.andrei1058.discordpublicservers.commands.service;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class Stats extends Command {

    public Stats(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!sender.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())) return;
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(getConfig().getColor());
            eb.setTitle("Service statistics");
            eb.setThumbnail(getConfig().getLogo());
            eb.addField("Database Servers", String.valueOf(getDatabase().getDatabaseServers()), true);
            eb.addField("Pinky Servers", String.valueOf(getBot().getGuilds().size()), true);
            eb.addField("Displayed Servers", String.valueOf(getDatabase().getDisplayedGuilds()), true);
            eb.addField("Premium Servers", String.valueOf(getDatabase().getPremiumGuilds()), true);
            c.sendMessage(eb.build()).queue();
        } else {
            c.sendMessage("**Service statistics**\n" +
                    "Database Servers: `"+getDatabase().getDatabaseServers()+"`\n" +
                    "Pinky Servers: `"+getBot().getGuilds().size()+"`\n" +
                    "Displayed Servers: `"+getDatabase().getDisplayedGuilds()+"`\n" +
                    "Premium Servers: `"+getDatabase().getPremiumGuilds()+"`\n").queue();
        }
    }
}
