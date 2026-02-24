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

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("An error occurred while closing the connection");
            e.printStackTrace();
        }
    }

    public boolean tableExists(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet ignored = statement.executeQuery("SELECT * FROM " + tableName + ";")) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean columnExists(String tableName, String columnName) {
        try (Statement statement = connection.createStatement();
             ResultSet ignored = statement.executeQuery("SELECT " + columnName + " FROM " + tableName + ";")) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addColumn(String tableName, String columnName, String type) {
        if (columnExists(tableName, columnName)) return false;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + ";");
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public int executeUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
