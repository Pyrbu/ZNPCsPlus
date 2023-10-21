package lol.pyr.znpcsplus.storage.mysql;

import lol.pyr.znpcsplus.storage.database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class MySQL extends Database {
    private final String connectionURL;

    public MySQL(String connectionURL, Logger logger) {
        super(logger);
        this.connectionURL = connectionURL;
    }

    @Override
    public Connection getSQLConnection() {
        validateConnectionUrl();

        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = java.sql.DriverManager.getConnection(connectionURL);
            return connection;
        } catch (ClassNotFoundException ex) {
            logger.severe("MySQL JDBC library not found" + ex);
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("08006")) {
                logger.severe("Could not connect to MySQL server. Check your connection settings and make sure the server is online.");
            } else if (ex.getSQLState().equals("08002")) {
                logger.severe("A connection already exists." + ex);
            } else {
                logger.severe("MySQL exception on initialize" + ex);
            }
        }
        return null;
    }

    private void validateConnectionUrl() {
        if (connectionURL == null || connectionURL.isEmpty()) {
            throw new IllegalArgumentException("Connection URL cannot be null or empty");
        }
        if (!connectionURL.startsWith("jdbc:mysql://")) {
            throw new IllegalArgumentException("Connection URL must start with 'jdbc:mysql://'");
        }
        // TODO: Validate the rest of the URL
    }

    @Override
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

    public int executeUpdate(String sql) {
        try {
            Statement s = connection.createStatement();
            int rowCount = s.executeUpdate(sql);
            s.close();
            return rowCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
