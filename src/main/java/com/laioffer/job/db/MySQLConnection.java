package com.laioffer.job.db;

import com.laioffer.job.entity.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//users click at "save" / "unsave" on frontend, and we will need servlets to update db on backend
public class MySQLConnection {
    private Connection conn;

    public MySQLConnection() {
        //try, catch: try{} and if fails then do catch{}
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(MySQLDBUtil.URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void saveItem(Item item) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }
        String insertItemSql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?)"; //"?" : placeholder, columns
                                                                                  //ignore: if there's a same item existed, we ignore it
        try {
            PreparedStatement statement = conn.prepareStatement(insertItemSql);
            statement.setString(1, item.getId());
            statement.setString(2, item.getTitle());
            statement.setString(3, item.getLocation());
            statement.setString(4, item.getCompanyLogo());
            statement.setString(5, item.getUrl()); //SQL starts from 1, not 0
            statement.executeUpdate(); //insert data completed
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertKeywordSql = "INSERT IGNORE INTO keywords VALUES (?, ?)"; //"?" : placeholder, columns
                                                                              // ignore: if there's a same item existed, we ignore it
        try {
            for (String keyword : item.getKeywords()) {
                PreparedStatement statement = conn.prepareStatement(insertKeywordSql);
                statement = conn.prepareStatement(insertKeywordSql);
                statement.setString(1, item.getId());
                statement.setString(2, keyword);
                statement.executeUpdate();
            } //we use for loop to insert keywords
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setFavoriteItems(String userId, Item item) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }
        //save item to db first
        saveItem(item);
        String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?, ?)"; //time will automatically filled in
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void unsetFavoriteItems(String userId, String itemId) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }
        //no need to delete items from db
        String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?"; //?: input arguments
                                                                              //delete all items which match the user id and item id
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
