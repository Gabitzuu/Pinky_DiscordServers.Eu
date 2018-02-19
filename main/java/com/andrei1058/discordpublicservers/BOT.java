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

package com.andrei1058.discordpublicservers;

import com.andrei1058.discordpublicservers.commands.Help;
import com.andrei1058.discordpublicservers.configuration.Config;
import com.andrei1058.discordpublicservers.configuration.Database;
import com.andrei1058.discordpublicservers.listeners.CollectData;
import com.andrei1058.discordpublicservers.listeners.Message;
import com.andrei1058.discordpublicservers.listeners.Ready;
import com.andrei1058.discordpublicservers.listeners.Shutdown;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class BOT {

    private static Config config;
    private static JDA bot;
    private static String version = "1.0beta";
    private static String latUpdate = "18/02/2018 UTC+2";
    private static Database database;


     public static void main(String[] args){
         config = new Config();
         try {
             bot = new JDABuilder(AccountType.BOT).setToken(getConfig().getToken()).buildAsync();
         } catch (LoginException e) {
             e.printStackTrace();
             return;
         }
         getBot().addEventListener(new Ready());
         getBot().addEventListener(new Message());
         getBot().addEventListener(new Shutdown());
         getBot().addEventListener(new CollectData());
         getBot().setAutoReconnect(true);
         new Help("help");
         database = new Database();

         //todo check for new servers to save
         //todo check for servers to remove
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
    }
}
