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

import java.io.IOException;

import static com.andrei1058.discordpublicservers.BOT.*;

public class Restart extends Command {

    public Restart(String name) {
        super(name);
    }

    @Override
    public void execute(String[] args, TextChannel c, Member sender, Guild g, String s) {
        if (!PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_WRITE)) /* todo msg can't write on this channel */
            return;
        if (!sender.getUser().getId().equalsIgnoreCase(getConfig().getOwnerID())) return;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(getConfig().getColor());
        if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
            eb.setDescription("Service restarting!");
            c.sendMessage(eb.build()).queue();
        } else {
            c.sendMessage("Service restarting!").queue();
        }
        log("Received restart command.");
        getRunnable().stop();
        getBot().shutdown();
        System.exit(0);
        Process runtime = null;
        try {
            runtime = Runtime.getRuntime().exec(getConfig().getRestartCmd());
        } catch (IOException e) {
            log(e.getMessage());
            e.printStackTrace();
            if (PermissionUtil.checkPermission(c, g.getSelfMember(), Permission.MESSAGE_EMBED_LINKS)) {
                eb.setDescription("Can't restart.\nService stopped!");
                c.sendMessage(eb.build()).queue();
                return;
            }
            c.sendMessage("Can't restart.\nService stopped!").queue();
        } finally {
            runtime.destroy();
        }
    }
}
