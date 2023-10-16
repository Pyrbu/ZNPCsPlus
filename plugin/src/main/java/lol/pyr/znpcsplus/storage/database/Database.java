package lol.pyr.znpcsplus.storage.database;

import java.sql.Connection;
import java.util.logging.Logger;

public abstract class Database {
    protected final Logger logger;
    protected Connection connection;
    public Database(Logger logger){
        this.logger = logger;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();
}
