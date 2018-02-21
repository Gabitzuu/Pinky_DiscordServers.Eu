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

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Base64;

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
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS servers (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, server_id BIGINT(200), added_date TIMESTAMP, " +
                    "server_name VARCHAR(200), server_desc VARCHAR(200), on_users INT(200), tot_users INT(200), bots INT(200), last_bump TIMESTAMP, last_update TIMESTAMP, votes INT(200), " +
                    "premium INT(1), owner_id BIGINT(200), owner_name VARCHAR(200), invite_link VARCHAR(200), server_icon VARCHAR(200), tags VARCHAR(200), langs VARCHAR(200), display INT(1));");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS banned_servers (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, server_id BIGINT(200), date TIMESTAMP, reason VARCHAR(200));");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS banned_users (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id BIGINT(200), date TIMESTAMP, reason VARCHAR(200));");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS feedback (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id BIGINT(200), user_name VARCHAR(200), message VARCHAR(200));");
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
            return new String(Base64.getDecoder().decode(rs.getString(1)));
        } catch (SQLException e) {
            log(e.getMessage());
            return "";
        }
    }

    public String getUserBanReason(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT reason FROM banned_users WHERE server_id='"+id+"';");
            return new String(Base64.getDecoder().decode(rs.getString(1)));
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
            ps.setString(3, Base64.getEncoder().encode(reason.getBytes("UTF-8")).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log(e.getMessage());
        }
    }

    public void banGuild(long id, String reason){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO banned_servers VALUES(?,?,?);");
            ps.setInt(1, 0);
            ps.setLong(2, id);
            ps.setString(3, Base64.getEncoder().encode(reason.getBytes("UTF-8")).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, Base64.getEncoder().encode(server_name.getBytes("UTF-8")).toString());
            ps.setString(5, Base64.getEncoder().encode("A new Discord server :D".getBytes("UTF-8")).toString());
            ps.setInt(6, on_users);
            ps.setInt(7, tot_users);
            ps.setInt(8, bots);
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
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
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS sv"+server_id+" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id bigint(200), date timestamp);");
        } catch (SQLException e) {
            log(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateGuildData(long server_id, String server_name, int on_users, int tot_users, int bots, long owner_id, String owner_name, String invite_link, String server_icon){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET server_name=?, on_users=?, tot_users=?, bots=?, owner_id=?, owner_name=?, last_update=?, invite_link=?, server_icon=? WHERE server_id=?;");
            ps.setString(1, Base64.getEncoder().encode(server_name.getBytes("UTF-8")).toString());
            ps.setInt(2, on_users);
            ps.setInt(3, tot_users);
            ps.setInt(4, bots);
            ps.setLong(5, owner_id);
            ps.setString(6, owner_name);
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setString(8, invite_link);
            ps.setString(9, server_icon);
            ps.setLong(10, server_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateMembersCount(String id, int on, int tot){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET on_users=?, tot_users=? WHERE server_id='"+id+"';");
            ps.setInt(1, on);
            ps.setInt(2, tot);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void updateBotsCount(String id, int bots){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET bots=? WHERE server_id='"+id+"';");
            ps.setInt(1, bots);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
        }
    }

    public void updateGuildName(String id, String var){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET server_name=? WHERE server_id='"+id+"';");
            ps.setString(1, Base64.getEncoder().encode(var.getBytes("UTF-8")).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateGuildOwner(String id, String var, long owner_id){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET owner_id=?, owner_name=? WHERE server_id='"+id+"';");
            ps.setLong(1, owner_id);
            ps.setString(2, var);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateGuildIcon(String id, String var){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET server_icon=? WHERE server_id='"+id+"';");
            ps.setString(1, var);
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateLastTime(String id){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET last_update=? WHERE server_id='"+id+"';");
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void hideGuild(String id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("UPDATE servers SET display='0', WHERE server_id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showGuild(String id){
        if (!isConnected()) connect();
        try {
            connection.createStatement().executeUpdate("UPDATE servers SET display='1', WHERE server_id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateDesc(String id, String desc){
        if (!isConnected()) connect();
        try {
            byte[] d = new byte[0];
            try {
                d = desc.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                log(e.getMessage());
            }
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET server_desc=? WHERE server_id='"+id+"';");
            ps.setString(1, Base64.getEncoder().encodeToString(d));
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public Timestamp getLastBump(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT last_bump FROM servers WHERE server_id='"+id+"';");
            if (rs.next()){
                return rs.getTimestamp(1);
            }
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateTags(String id, String var){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET tags=? WHERE server_id='"+id+"';");
            ps.setString(1, var.toUpperCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateLanguage(String id, String var){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET langs=? WHERE server_id='"+id+"';");
            ps.setString(1, var.toUpperCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isShown(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT server_name FROM servers WHERE server_id='"+id+"';");
            return rs.next();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isPremium(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT premium FROM servers WHERE server_id='"+id+"';");
            if (rs.next()){
                if (rs.getInt(1) == 1) return true;
            }
            return false;
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void bumpGuild(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(id) FROM servers");
            connection.createStatement().executeUpdate("UPDATE servers SET id='"+rs.getInt(1)+"', WHERE server_id='"+id+"';");
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void voteGuild(String string, long user){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sv"+string+" VALUES (?, ?, ?);");
            ps.setInt(1, 0);
            ps.setLong(2, user);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (Exception e){
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean hasVote(String id, long user){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM sv"+id+" WHERE user_id='"+user+"';");
            return rs.next();
        } catch (Exception e){
            log(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Timestamp getVoteDate(String id, long user){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT date FROM sv"+id+" WHERE user_id='"+user+"';");
            if (rs.next()){
                return rs.getTimestamp(1);
            }
        } catch (Exception e){
            log(e.getMessage());
            e.printStackTrace();
        }
        return new Timestamp(System.currentTimeMillis());
    }

    public int getVotes(String id){
        if (!isConnected()) connect();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(id) FROM sv"+id+";");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log(e.getMessage());
        }
        return 1;
    }

    public void addFeedback(String user_name, long user_id, String message){
        if (!isConnected()) connect();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO feedback VALUES (?, ?, ?, ?);");
            ps.setInt(1, 0);
            ps.setLong(2, user_id);
            ps.setString(3, user_name);
            ps.setString(4, Base64.getEncoder().encode(message.getBytes("UTF-8")).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            log(e.getMessage());
            e.printStackTrace();
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
