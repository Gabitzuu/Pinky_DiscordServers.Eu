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

package com.andrei1058.discordpublicservers.commands.member;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.sql.Timestamp;

import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.getDatabase;
import static com.andrei1058.discordpublicservers.BOT.log;

public class Vote extends Command {

    public Vote(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!getDatabase().isGuildExists(g.getId())) return;
        /** check if bot can send embed links*/
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            if (g.getOwner().getUser().getIdLong() == sender.getUser().getIdLong()){
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sorry");
                eb.setDescription("You can't vote your own server!");
                eb.setColor(getConfig().getColor());
                c.sendMessage(eb.build()).queue();
                return;
            }
            if (getDatabase().hasVote(g.getId(), sender.getUser().getIdLong())) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sorry");
                Timestamp t = getDatabase().getVoteDate(g.getId(), sender.getUser().getIdLong());
                eb.setDescription("You've already voted this server on " + t.toLocalDateTime().getMonth() + " " + t.toLocalDateTime().getDayOfMonth() + " " + t.toLocalDateTime().getYear() + " UTC +1");
                eb.setColor(getConfig().getColor());
                c.sendMessage(eb.build()).queue();
            } else {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(getConfig().getColor());
                eb.setTitle("Thanks");
                eb.setDescription("Your vote was added :smiley:\n" + g.getName() + " total votes: " + (getDatabase().getVotes(g.getId())+1));
                c.sendMessage(eb.build()).queue();
                getDatabase().voteGuild(g.getId(), sender.getUser().getIdLong());
                log("New vote: " + g.getName() + " - " + sender.getUser().getName());
            }
        } else {
            if (g.getOwner().getUser().getIdLong() == sender.getUser().getIdLong()){
                c.sendMessage("Sorry\nYou can't vote your own server!").queue();
                return;
            }
            Timestamp t = getDatabase().getVoteDate(g.getId(), sender.getUser().getIdLong());
            if (getDatabase().hasVote(g.getId(), sender.getUser().getIdLong())) {
                c.sendMessage("Sorry\n" +
                        "You've already voted this server on `" + t.toLocalDateTime().getMonth() + " " + t.toLocalDateTime().getDayOfMonth() + " " + t.toLocalDateTime().getYear() + "` UTC +1").queue();
            } else {
                c.sendMessage("Thanks\n" +
                        "Your vote was added :smiley:\n" +
                        "`" + g.getName() + "` total votes: `" + (getDatabase().getVotes(g.getId())+1) + "`").queue();
                getDatabase().voteGuild(g.getId(), sender.getUser().getIdLong());
                log("New vote: " + g.getName() + " - " + sender.getUser().getName());
            }
        }
    }
}
