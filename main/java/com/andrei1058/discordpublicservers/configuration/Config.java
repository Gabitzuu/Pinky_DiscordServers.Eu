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

package com.andrei1058.discordpublicservers.configuration;

import java.io.*;
import java.util.Properties;

public class Config {

    Properties properties;
    OutputStream out;
    InputStream in;

    private String token, host, port, user, pass, statusType, statusMsg, streamLink;

    public Config() {
        properties = new Properties();

        /* create file */
        File botProp = new File("bot.properties");
        if (!botProp.exists()){
            try {
                botProp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out = new FileOutputStream("bot.properties");
            in = new FileInputStream("bot.properties");
            properties.load(in);

            /* add defaults */
            if (properties.get("token") == null) {
                properties.setProperty("token", "md9032jnf389n3f34nf4389nf893nf3");
            } else {
                token = properties.getProperty("token");
            }

            if (properties.get("mysql-host") == null){
                properties.setProperty("mysql-host", "localhost");
            } else {
                host = properties.getProperty("mysql-host");
            }

            if (properties.get("mysql-port") == null){
                properties.setProperty("mysql-port", "3306");
            } else {
                port = properties.getProperty("mysql-port");
            }

            if (properties.get("mysql-user") == null){
                properties.setProperty("mysql-user", "root");
            } else {
                user = properties.getProperty("mysql-user");
            }

            if (properties.get("mysql-pass") == null){
                properties.setProperty("mysql-pass", "pass");
            } else {
                pass = properties.getProperty("mysql-pass");
            }

            if (properties.get("status-type") == null){
                properties.setProperty("status-type", "PLAYING"); //todo playing, streaming
            } else {
                statusType = properties.getProperty("status-type");
            }

            if (properties.get("status-msg") == null){
                properties.setProperty("status-msg", "on {servers} servers"); //todo PLACEHOLDERS: {servers} - svs nr
            } else {
                statusMsg = properties.getProperty("status-msg");
            }

            if (properties.get("stream-link") == null){
                properties.setProperty("stream-link", "null"); //todo if status-type == streaming
            } else {
                streamLink = properties.getProperty("stream-link");
            }

            properties.store(out, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String getHost() {
        return host;
    }

    public String getToken() {
        return token;
    }

    public String getPass() {
        return pass;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public String getStatusType() {
        return statusType;
    }

    public String getStreamLink() {
        return streamLink;
    }
}
