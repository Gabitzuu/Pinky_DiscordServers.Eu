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

package com.andrei1058.discordpublicservers;

import com.andrei1058.discordpublicservers.configuration.Config;
import com.andrei1058.discordpublicservers.configuration.Database;
import com.andrei1058.discordpublicservers.misc.ExpireRefresh;
import com.andrei1058.discordpublicservers.misc.GeneralRefresh;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.andrei1058.discordpublicservers.misc.Misc.createStartScript;
import static com.andrei1058.discordpublicservers.misc.Misc.registerCommands;
import static com.andrei1058.discordpublicservers.misc.Misc.registerEvents;

public class BOT {

    private static Config config;
    private static JDA bot;
    private static String version = "1.1";
    private static String latUpdate = "23/02/2018 UTC+1";
    private static Database database;
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static GeneralRefresh runnable = new GeneralRefresh();
    private static ExpireRefresh expireRefresh = new ExpireRefresh();


     public static void main(String[] args){
         config = new Config();
         try {
             bot = new JDABuilder(AccountType.BOT).setToken(getConfig().getToken()).buildAsync();
         } catch (LoginException e) {
             e.printStackTrace();
             return;
         }
         registerEvents();
         getBot().setAutoReconnect(true);
         registerCommands();
         database = new Database();
         scheduler.schedule(runnable, 24, TimeUnit.HOURS);
         scheduler.schedule(expireRefresh, 1, TimeUnit.HOURS);
         createStartScript();
     }

    public static Config getConfig() {
        return config;
    }

    public static JDA getBot() {
        return bot;
    }

    public static String getLatUpdate() {
        return latUpdate;
    }

    public static String getVersion() {
        return version;
    }

    public static Database getDatabase() {
        return database;
    }

    public static void log(String message){
         System.out.println(message);
         getBot().getGuildById("310868476958998528").getTextChannelById("318363810367012866").sendMessage(message).queue();
    }

    public static GeneralRefresh getRunnable() {
        return runnable;
    }
}
