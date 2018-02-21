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

import net.dv8tion.jda.core.OnlineStatus;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class Config {

    Properties properties;
    OutputStream out;
    InputStream in;

    private String token, host, port, user, pass, statusType, statusMsg, streamLink, ownerID;
    private String logo = "https://discordpublicservers.com/inc/logo.png", serviceLink = "https://discordpublicservers.com",
    restartCmd = "./start.sh";
    private OnlineStatus status;
    private Color color = Color.PINK;

    public Config() {
        properties = new Properties();

        /* create file */
        File botProp = new File("bot.properties");
        if (!botProp.exists()) {
            try {
                botProp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            /* add defaults */
        token = setupProp("token", "sadasd3d23");
        host = setupProp("mysql-host", "localhost");
        port = setupProp("mysql-port", "3306");
        user = setupProp("mysql-user", "root");
        pass = setupProp("mysql-pass", "pass");
        statusMsg = setupProp("status-msg", "on {servers} servers");
        statusType = setupProp("status-type", "PLAYING");
        streamLink = setupProp("stream-link", "nulll");
        ownerID = setupProp("owner-ID", "178199397782257665");
        openIn();
        if (properties.get("status") != null) {
            switch (properties.getProperty("status").toLowerCase()) {
                default:
                    status = OnlineStatus.ONLINE;
                    break;
                case "do_not_disturb":
                case "donotdisturb":
                case "busy":
                    status = OnlineStatus.DO_NOT_DISTURB;
                    break;
                case "idle":
                    status = OnlineStatus.IDLE;
                    break;
                case "offline":
                    status = OnlineStatus.OFFLINE;
                    break;
            }
        } else {
            closeIn();
            openOut();
            properties.setProperty("status", "ONLINE");
            status = OnlineStatus.ONLINE;
            closeOut();
        }
        closeIn();
    }

    private String setupProp(String path, String value) {
        openIn();
        if (properties.get(path) == null) {
            closeIn();
            openOut();
            properties.setProperty(path, value);
            closeOut();
        }
        return (String) properties.get(path);
    }

    private void closeIn() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openIn() {
        try {
            in = new FileInputStream("bot.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openOut() {
        try {
            out = new FileOutputStream("bot.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeOut() {
        try {
            properties.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    public OnlineStatus getStatus() {
        return status;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getLogo() {
        return logo;
    }

    public String getServiceLink() {
        return serviceLink;
    }

    public Color getColor() {
        return color;
    }

    public String getRestartCmd() {
        return restartCmd;
    }
}
