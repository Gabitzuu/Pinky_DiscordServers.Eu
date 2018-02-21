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

package com.andrei1058.discordpublicservers.commands.server;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static com.andrei1058.discordpublicservers.BOT.*;

public class Feedback extends Command {

    public Feedback(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!sender.hasPermission(Permission.MANAGE_ROLES)) return;
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            EmbedBuilder eb = new EmbedBuilder();
            if (s.isEmpty() || args.length < 3) {
                eb.setTitle("Sorry");
                eb.setColor(getConfig().getColor());
                eb.setDescription("Your message is too short!\nExample: 00feedback Keep up the good work!");
                c.sendMessage(eb.build()).queue();
                return;
            }
            eb.setTitle("Thanks");
            eb.setColor(getConfig().getColor());
            eb.setDescription("Your feedback was sent!");
            c.sendMessage(eb.build()).queue();
        } else {
            if (s.isEmpty() || args.length < 3) {
                c.sendMessage("Sorry :frowning:\nYour message is too short.\nExample: `00feedback Keep up the good work!`").queue();
                return;
            }
            c.sendMessage("Thanks :smiley:\nYour feedback was sent!").queue();
        }
        getDatabase().addFeedback(sender.getUser().getName(), sender.getUser().getIdLong(), s);
        log("New feedback: "+s);
        try {
            PrivateChannel p = getBot().getUserById(getConfig().getOwnerID()).openPrivateChannel().complete();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Feedback");
            eb.setColor(getConfig().getColor());
            eb.setDescription(s);
            eb.setFooter(sender.getUser().getName()+"#"+sender.getUser().getDiscriminator(), sender.getUser().getAvatarUrl());
            p.sendMessage(eb.build()).queue();
        } catch (Exception e){
            log(e.getMessage());
            e.printStackTrace();
        }
    }
}
