package lol.pyr.znpcsplus.storage.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import lol.pyr.znpcsplus.storage.database.Database;

public class MySQL extends Database {
  private final String connectionURL;
  private final String username;
  private final String password;

  public MySQL(String connectionURL, String username, String password, Logger logger) {
    super(logger);
    this.connectionURL = connectionURL;
    this.username = username;
    this.password = password;
  }

  @Override
  public Connection getSQLConnection() {
    validateConnectionUrl();

    try {
      if (connection != null && !connection.isClosed()) {
        return connection;
      }
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = java.sql.DriverManager.getConnection(connectionURL, username, password);
      return connection;
    } catch (ClassNotFoundException ex) {
      logger.severe("MySQL JDBC library not found" + ex);
    } catch (SQLException ex) {
      String state = ex.getSQLState();
      if ("08006".equals(state)) {
        logger.severe(
            "Could not connect to MySQL server. Check your connection settings and make sure the server is online.");
      } else if ("08002".equals(state)) {
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
        ResultSet ignored =
            statement.executeQuery("SELECT " + columnName + " FROM " + tableName + ";")) {
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public boolean addColumn(String tableName, String columnName, String type) {
    if (columnExists(tableName, columnName)) return false;
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(
          "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + ";");
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

  public int executeUpdate(String sql) {
    try (Statement statement = connection.createStatement()) {
      return statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
