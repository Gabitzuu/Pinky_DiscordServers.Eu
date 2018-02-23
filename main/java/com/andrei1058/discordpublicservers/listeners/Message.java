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

package com.andrei1058.discordpublicservers.listeners;

import com.andrei1058.discordpublicservers.commands.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Message extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        /* private channel commands */
        if (e.getChannel().getType() == ChannelType.PRIVATE){

            return;
        }
        /* guild channel commands */
        if (e.getMessage().getContentRaw().startsWith("00")){
            String var = e.getMessage().getContentRaw().substring(2, e.getMessage().getContentRaw().length());
            if (!var.isEmpty()){
                String[] var2 = var.split(" ");
                var = var.replaceFirst(var2[0], "");
                if (var.startsWith(" ")) var = var.replaceFirst(" ", "");
                String[] args = var.split(" ");
                for (Command c : Command.getCommandList()){
                    if (c.getName().equalsIgnoreCase(var2[0])){
                        c.execute(args, e.getTextChannel(), e.getMember(), e.getGuild(), var);
                    }
                }
            }
        }
    }
}
