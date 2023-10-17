package lol.pyr.znpcsplus.storage.sqlite;

import lol.pyr.znpcsplus.storage.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

public class SQLite extends Database{
    private final File dbFile;
    public SQLite(File file, Logger logger){
        super(logger);
        dbFile = file;
    }

    public Connection getSQLConnection() {
        if (!dbFile.exists()){
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                logger.severe("File write error: "+dbFile.getName());
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            return connection;
        } catch (SQLException ex) {
            logger.severe("SQLite exception on initialize" + ex);
        } catch (ClassNotFoundException ex) {
            logger.severe("SQLite JDBC library not found" + ex);
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
    }

    public boolean tableExists(String tableName) {
        try {
            Statement s = connection.createStatement();
            s.executeQuery("SELECT * FROM " + tableName + ";");
            s.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean columnExists(String tableName, String columnName) {
        try {
            Statement s = connection.createStatement();
            s.executeQuery("SELECT " + columnName + " FROM " + tableName + ";");
            s.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addColumn(String tableName, String columnName, String type) {
        if (columnExists(tableName, columnName)) return false;
        try {
            Statement s = connection.createStatement();
            s.executeQuery("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + ";");
            s.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            s.close();
            return rs;
        } catch (SQLException e) {
            return null;
        }
    }

    public int executeUpdate(String query) {
        try {
            Statement s = connection.createStatement();
            int rowCount = s.executeUpdate(query);
            s.close();
            return rowCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
