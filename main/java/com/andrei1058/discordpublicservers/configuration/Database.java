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

import java.sql.*;

import static com.andrei1058.discordpublicservers.BOT.getBot;
import static com.andrei1058.discordpublicservers.BOT.getConfig;
import static com.andrei1058.discordpublicservers.BOT.log;

public class Database {

    private Connection connection;

    public Database(){
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!connect()){
           log("Can't connect to database!");
           getBot().shutdownNow();
           return;
        }
        setupDatabase();
    }

    private void setupDatabase(){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS servers (id int not null auto_increment primary key, server_id bigint(200), added_date timestamp, " +
                    "server_name varchar(200), server_desc varchar(200), on_users int(200), tot_users int(200), bots int(200), last_bump timestamp, last_update timestamp, votes int(200), " +
                    "premium int(1), owner_id bigint(200), owner_name varchar(200), invite_link varchar(200), server_icon varchar(200), tags varchar(200), langs varchar(200), display int(1));");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS banned_servers (id int not null auto_increment primary key, server_id bigint(200), date timestamp, reason varchar(200));");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS banned_users (id int not null auto_increment primary key, user_id bigint(200), date timestamp, reason varchar(200));");
        } catch (SQLException e) {
           log(e.getMessage());
        }
    }

    public boolean isGuildExists(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT server_name FROM servers WHERE server_id='"+id+"';");
            return rs.next();
        } catch (Exception ex){
            log(ex.getMessage());
            return false;
        }
    }

    public boolean isGuildBanned(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT reason FROM banned_servers WHERE server_id='"+id+"';");
            return rs.next();
        } catch (SQLException e) {
            log(e.getMessage());
            return false;
        }
    }

    public String getGuildBanReason(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT reason FROM banned_servers WHERE server_id='"+id+"';");
            return rs.getString(1);
        } catch (SQLException e) {
            log(e.getMessage());
            return "";
        }
    }

    public String getUserBanReason(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT reason FROM banned_users WHERE server_id='"+id+"';");
            return rs.getString(1);
        } catch (SQLException e) {
            log(e.getMessage());
            return "";
        }
    }

    public boolean isUserBanned(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT reason FROM banned_users WHERE user_id='"+id+"';");
            return rs.next();
        } catch (SQLException e) {
            log(e.getMessage());
            return false;
        }
    }

    public void banUser(long id, String reason){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO banned_users VALUES(?,?,?);");
            ps.setInt(1, 0);
            ps.setLong(2, id);
            ps.setString(3, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void banGuild(long id, String reason){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO banned_servers VALUES(?,?,?);");
            ps.setInt(1, 0);
            ps.setLong(2, id);
            ps.setString(3, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void unbanUser(long id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("DELETE FROM banned_users WHERE id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void unbanGuild(long id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("DELETE FROM banned_servers WHERE id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void addNewServer(long server_id, String server_name, int on_users, int tot_users, int bots, long owner_id, String owner_name, String invite_link, String server_icon, String tags, String langs){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO servers VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            ps.setInt(1, 0);
            ps.setLong(2, server_id);
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setString(4, server_name);
            ps.setString(5, "A new Discord server :D");
            ps.setInt(6, on_users);
            ps.setInt(7, tot_users);
            ps.setInt(8, bots);
            ps.setDate(9, new Date(System.currentTimeMillis()));
            ps.setDate(10, new Date(System.currentTimeMillis()));
            ps.setInt(11, 0);
            ps.setInt(12, 0);
            ps.setLong(13, owner_id);
            ps.setString(14, owner_name);
            ps.setString(15, invite_link);
            ps.setString(16, server_icon);
            ps.setString(17, tags);
            ps.setString(18, langs);
            ps.setInt(19, 1);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void updateGuildData(long server_id, String server_name, int on_users, int tot_users, int bots, long owner_id, String owner_name, String invite_link, String server_icon){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET server_name=?, on_users=?, tot_users=?, bots=?, owner_id=?, owner_name=?, invite_link=?, server_icon=? WHERE server_id=?;");
            ps.setString(1, server_name);
            ps.setInt(2, on_users);
            ps.setInt(3, tot_users);
            ps.setInt(4, bots);
            ps.setLong(5, owner_id);
            ps.setString(6, owner_name);
            ps.setString(7, invite_link);
            ps.setString(8, server_icon);
            ps.setLong(9, server_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void hideGuild(String id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("UPDATE servers SET display='0', WHERE server_id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void showGuild(String id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("UPDATE servers SET display='1', WHERE server_id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public boolean isShown(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT server_name FROM servers WHERE server_id='"+id+"'");
            return rs.next();
        } catch (SQLException e) {
            log(e.getMessage());
            return false;
        }
    }

    public boolean connect(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+getConfig().getHost()+":"+getConfig().getPort()+"/"+"discordservers?autoReconnect=true",
                    getConfig().getUser(), getConfig().getPass());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        if (!isConnected()) return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return connection != null;
    }
}
