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
import com.andrei1058.discordpublicservers.customisation.Messages;
import com.andrei1058.discordpublicservers.misc.Misc;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;

public class MakePremium extends Command {

    public MakePremium(String name) {
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
            if (args.length > 1){
                if (!Misc.isLong(args[0])){
                    eb.setTitle("Sorry");
                    eb.setDescription("id must be a long");
                    c.sendMessage(eb.build()).queue();
                    return;
                }
                if (!Misc.isInt(args[1])){
                    eb.setTitle("Sorry");
                    eb.setDescription("duration must be an int");
                    c.sendMessage(eb.build()).queue();
                    return;
                }
                Guild g1 = getBot().getGuildById(args[0]);
                if (g1 == null){
                    eb.setTitle("Sorry");
                    eb.setDescription("I'm not on this server :frowning:");
                    c.sendMessage(eb.build()).queue();
                    return;
                }
                eb.setTitle("Added premium server");
                eb.setDescription(getBot().getGuildById(args[0]).getName()+" is now premium!\nExpires in "+args[1]+" days.");
                c.sendMessage(eb.build()).queue();
                getDatabase().addPremiumGuild(Long.valueOf(args[0]), Integer.valueOf(args[1]));
                Messages.send(g1, g1.getOwner().getUser(), Messages.Message.BOUGHT_PREMIUM);
                return;
            }
            eb.setTitle("Sorry");
            eb.setDescription("Insufficient arguments.\nUsage: 00makePremium id duration");
            c.sendMessage(eb.build()).queue();
        } else {
            if (args.length > 1){
                if (!Misc.isLong(args[0])){
                    c.sendMessage("Sorry! id must be a long.").queue();
                    return;
                }
                if (!Misc.isInt(args[1])){
                    c.sendMessage("Sorry! duration must be an int.").queue();
                    return;
                }
                Guild g1 = getBot().getGuildById(args[0]);
                if (g1 == null){
                    c.sendMessage("Sorry! I'm not on this server :frowning:").queue();
                    return;
                }
                c.sendMessage(getBot().getGuildById(args[0]).getName()+" is now premium!\nExpires in "+args[1]+" days.").queue();
                getDatabase().addPremiumGuild(Long.valueOf(args[0]), Integer.valueOf(args[1]));
                Messages.send(g1, g1.getOwner().getUser(), Messages.Message.BOUGHT_PREMIUM);
                return;
            }
            c.sendMessage("Sorry! Insufficient arguments.\nUsage: 00makePremium id duration").queue();
        }
    }
}
