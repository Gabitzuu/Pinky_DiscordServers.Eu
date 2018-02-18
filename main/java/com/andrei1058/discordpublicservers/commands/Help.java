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
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;

public class Help extends Command {

    public Help(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) return;
        /** check if bot can send embed links*/
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            /** service owner commands*/
            if (sender.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("DiscordPublicServers.Com", getConfig().getLogo());
                eb.setThumbnail(getConfig().getLogo());
                eb.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                eb.addField("00stop", "Shutdown dps.", true);
                eb.addField("00restart", "Restart dps.", true);
                eb.addField("00banUser", "Ban a user.", true);
                eb.addField("00unbanUser", "UnBan a user.", true);
                eb.addField("00banServer", "Ban a server.", true);
                eb.addField("00unbanServer", "UnBan a server", true);
                eb.addField("00makePremium", "Give premium features.", true);
                eb.addField("00delPremium", "Del. premium features.", true);
                eb.addField("00stats", "Service statistics.", true);
                eb.addField("00removeSv", "Remove a sv.", true);
                eb.addField("00setStatus", "Change status.", true);
                eb.setDescription("Bot version: " + BOT.getVersion() + " - " + BOT.getLatUpdate());
                eb.setColor(Color.PINK);
                eb.setFooter("Service staff commands.", sender.getUser().getAvatarUrl());
                c.sendMessage(eb.build()).queue();
            }
            /** server staff commands*/
            if (sender.hasPermission(Permission.MANAGE_ROLES)){
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("DiscordPublicServers.Com", getConfig().getLogo());
                eb.setThumbnail(getConfig().getLogo());
                eb.setAuthor(getBot().getSelfUser().getName(), "https://discordpublicservers.com", getBot().getSelfUser().getAvatarUrl());
                eb.addField("00bump", "Bump this sv.", true);
                eb.addField("00setLang", "Set sv languages.", true);
                eb.addField("00setTags", "Set sv categories.", true);
                eb.addField("00setDesc", "Set description.", true);
                eb.setDescription("Bot version: " + BOT.getVersion() + " - " + BOT.getLatUpdate());
                eb.setColor(Color.PINK);
                eb.setFooter("Server staff commands.", sender.getUser().getAvatarUrl());
                c.sendMessage(eb.build()).queue();
            }
        } else {
            /** service owner commands*/
            if (sender.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())) {
                c.sendMessage("**DiscordPublicServers.Com**\n  Service staff commands. \n\n" +
                        "``00stop`` - Shutdown dps. \n" +
                        "``00restart`` - Restart dps. \n" +
                        "``00banUser`` - Ban a user. \n" +
                        "``00unbanUser`` - UnBan a user. \n" +
                        "``00banServer`` - Ban a server. \n" +
                        "``00unbanServer`` - UnBan a server. \n" +
                        "``00makePremium`` - Give premium features. \n" +
                        "``00delPremium`` - Delete premium features. \n" +
                        "``00stats`` - Service statistics. \n" +
                        "``00removeSv`` - Remove a server. \n" +
                        "``00setStatus`` - Change status. \n\n" +
                        "Bot version: " + BOT.getVersion() + " - " + BOT.getLatUpdate()).queue();
            }
            /** server staff commands */
            if (sender.hasPermission(Permission.MANAGE_ROLES)){
                c.sendMessage("**DiscordPublicServers.Com**\n   Server staff commands. \n\n" +
                        "``00bump`` -Bump this sv. \n" +
                        "``00setLang`` - Set sv languages. \n" +
                        "``00setTags`` -Set sv categories. \n" +
                        "``00setDesc`` - Set description. \n\n" +
                        "Bot version: " + BOT.getVersion() + " - " + BOT.getLatUpdate()).queue();
            }
        }
    }
}
