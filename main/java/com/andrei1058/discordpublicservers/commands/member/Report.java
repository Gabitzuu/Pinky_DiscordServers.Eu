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

package com.andrei1058.discordpublicservers.commands.member;

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

public class Report extends Command {

    public Report(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!getDatabase().isGuildExists(g.getId())) return;
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            EmbedBuilder e = new EmbedBuilder();
            e.setColor(getConfig().getColor());
            if (s.isEmpty()){
                e.setTitle("Error");
                e.setDescription("Please write a reason.\nUsage: 00report reason");
                c.sendMessage(e.build()).queue();
                return;
            }
            if (args.length < 5){
                e.setTitle("Error");
                e.setDescription("Please write a good reason.");
                c.sendMessage(e.build()).queue();
                return;
            }
            getDatabase().addReport(g.getIdLong(), s, sender.getUser().getIdLong());
            e.setTitle("Done");
            e.setDescription("Report sent!\nWe'll check if this server doesn't respect our tos.");
            c.sendMessage(e.build()).queue();
            log("New server report: "+g.getId()+" - "+s);
        } else {
            if (s.isEmpty()){
                c.sendMessage("Please write a reason.").queue();
                return;
            }
        }
    }
}
