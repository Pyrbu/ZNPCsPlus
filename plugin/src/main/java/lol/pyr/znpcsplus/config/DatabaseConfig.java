package lol.pyr.znpcsplus.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault.*;
import space.arim.dazzleconf.annote.ConfKey;

public interface DatabaseConfig {
    @ConfKey("host")
    @ConfComments("The host of the database")
    @DefaultString("localhost")
    String host();

    @ConfKey("port")
    @ConfComments("The port of the database")
    @DefaultInteger(3306)
    int port();

    @ConfKey("username")
    @ConfComments("The username to use to connect to the database")
    @DefaultString("znpcsplus")
    String username();

    @ConfKey("password")
    @ConfComments("The password to use to connect to the database")
    @DefaultString("password")
    String password();

    @ConfKey("database-name")
    @ConfComments("The name of the database to use")
    @DefaultString("znpcsplus")
    String databaseName();

    default String createConnectionURL(String dbType) {
        if (dbType.equalsIgnoreCase("mysql")) {
            return "jdbc:mysql://" + host() + ":" + port() + "/" + databaseName() + "?useSSL=false&user=" + username() + "&password=" + password();
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}

