package lol.pyr.znpcsplus.database;

import java.sql.Connection;
import java.util.logging.Logger;

public abstract class Database {
    Logger logger;
    Connection connection;
    public Database(Logger logger){
        this.logger = logger;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();
}
