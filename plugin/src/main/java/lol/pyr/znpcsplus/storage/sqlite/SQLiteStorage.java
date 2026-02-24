package lol.pyr.znpcsplus.storage.sqlite;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class SQLiteStorage implements NpcStorage {
  private static final Logger logger = Logger.getLogger("SQLiteStorage");

  private final PacketFactory packetFactory;
  private final ConfigManager configManager;
  private final ActionRegistryImpl actionRegistry;
  private final NpcTypeRegistryImpl typeRegistry;
  private final EntityPropertyRegistryImpl propertyRegistry;
  private final LegacyComponentSerializer textSerializer;
  private final SQLite database;

  private final String TABLE_NPCS;
  private final String TABLE_NPCS_PROPERTIES;
  private final String TABLE_NPCS_HOLOGRAMS;
  private final String TABLE_NPCS_ACTIONS;

  public SQLiteStorage(
      PacketFactory packetFactory,
      ConfigManager configManager,
      ActionRegistryImpl actionRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer,
      File file) {
    this.packetFactory = packetFactory;
    this.configManager = configManager;
    this.actionRegistry = actionRegistry;
    this.typeRegistry = typeRegistry;
    this.propertyRegistry = propertyRegistry;
    this.textSerializer = textSerializer;
    this.database = new SQLite(file, logger);
    database.load();
    if (database.getSQLConnection() == null) {
      throw new RuntimeException("Failed to initialize SQLite Storage.");
    }
    TABLE_NPCS = "npcs";
    TABLE_NPCS_PROPERTIES = "npcs_properties";
    TABLE_NPCS_HOLOGRAMS = "npcs_holograms";
    TABLE_NPCS_ACTIONS = "npcs_actions";
    validateTables();
  }

  private void validateTables() {
    if (!database.tableExists(TABLE_NPCS)) {
      logger.info("Creating table " + TABLE_NPCS + "...");
      createNpcsTable();
    }
    if (!database.tableExists(TABLE_NPCS_PROPERTIES)) {
      logger.info("Creating table " + TABLE_NPCS_PROPERTIES + "...");
      createNpcsPropertiesTable();
    }
    if (!database.tableExists(TABLE_NPCS_HOLOGRAMS)) {
      logger.info("Creating table " + TABLE_NPCS_HOLOGRAMS + "...");
      createNpcsHologramsTable();
    }
    if (!database.tableExists(TABLE_NPCS_ACTIONS)) {
      logger.info("Creating table " + TABLE_NPCS_ACTIONS + "...");
      createNpcsActionsTable();
    }
    updateTables();
  }

  private void createNpcsTable() {
    if (database.executeUpdate(
            "CREATE TABLE "
                + TABLE_NPCS
                + " "
                + "(id TEXT PRIMARY KEY, isProcessed BOOLEAN, allowCommands BOOLEAN, enabled BOOLEAN, "
                + "uuid TEXT, world TEXT, x REAL, y REAL, z REAL, yaw REAL, pitch REAL, type TEXT, hologramOffset REAL, hologramRefreshDelay INTEGER)")
        != -1) {
      logger.info("Table " + TABLE_NPCS + " created.");
    } else {
      logger.severe("Failed to create table " + TABLE_NPCS + ".");
    }
  }

  private void createNpcsPropertiesTable() {
    if (database.executeUpdate(
            "CREATE TABLE "
                + TABLE_NPCS_PROPERTIES
                + " "
                + "(npc_id TEXT, property TEXT, value TEXT, PRIMARY KEY (npc_id, property))")
        != -1) {
      logger.info("Table " + TABLE_NPCS_PROPERTIES + " created.");
    } else {
      logger.severe("Failed to create table " + TABLE_NPCS_PROPERTIES + ".");
    }
  }

  private void createNpcsHologramsTable() {
    if (database.executeUpdate(
            "CREATE TABLE "
                + TABLE_NPCS_HOLOGRAMS
                + " "
                + "(npc_id TEXT, line INTEGER, text TEXT, PRIMARY KEY (npc_id, line))")
        != -1) {
      logger.info("Table " + TABLE_NPCS_HOLOGRAMS + " created.");
    } else {
      logger.severe("Failed to create table " + TABLE_NPCS_HOLOGRAMS + ".");
    }
  }

  private void createNpcsActionsTable() {
    if (database.executeUpdate(
            "CREATE TABLE "
                + TABLE_NPCS_ACTIONS
                + " "
                + "(npc_id TEXT, action_id INTEGER, action_data TEXT, PRIMARY KEY (npc_id, action_id))")
        != -1) {
      logger.info("Table " + TABLE_NPCS_ACTIONS + " created.");
    } else {
      logger.severe("Failed to create table " + TABLE_NPCS_ACTIONS + ".");
    }
  }

  private void updateTables() {
    // Any table updates go here
  }

  @Override
  public Collection<NpcEntryImpl> loadNpcs() {
    Connection connection = database.getSQLConnection();
    if (connection == null) return Collections.emptyList();

    Map<String, NpcEntryImpl> npcMap = new HashMap<>();
    try (PreparedStatement st = connection.prepareStatement("SELECT * FROM " + TABLE_NPCS);
        ResultSet rs = st.executeQuery()) {
      while (rs.next()) {
        NpcImpl npc =
            new NpcImpl(
                UUID.fromString(rs.getString("uuid")),
                propertyRegistry,
                configManager,
                packetFactory,
                textSerializer,
                rs.getString("world"),
                typeRegistry.getByName(rs.getString("type")),
                new NpcLocation(
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch")));

        if (!rs.getBoolean("enabled")) npc.setEnabled(false);

        npc.getHologram().setOffset(rs.getDouble("hologramOffset"));
        if (rs.getBigDecimal("hologramRefreshDelay") != null)
          npc.getHologram().setRefreshDelay(rs.getBigDecimal("hologramRefreshDelay").longValue());

        NpcEntryImpl entry = new NpcEntryImpl(rs.getString("id"), npc);
        entry.setProcessed(rs.getBoolean("isProcessed"));
        entry.setAllowCommandModification(rs.getBoolean("allowCommands"));
        entry.setSave(true);
        npcMap.put(rs.getString("id"), entry);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try (PreparedStatement st =
            connection.prepareStatement("SELECT * FROM " + TABLE_NPCS_PROPERTIES);
        ResultSet rs = st.executeQuery()) {
      while (rs.next()) {
        NpcEntryImpl entry = npcMap.get(rs.getString("npc_id"));
        String key = rs.getString("property");
        if (entry != null) {
          EntityPropertyImpl<?> property = propertyRegistry.getByName(key);
          if (property == null) {
            logger.warning(
                "Unknown property '"
                    + key
                    + "' for npc '"
                    + rs.getString("npc_id")
                    + "'. skipping ...");
            continue;
          }
          PropertySerializer<?> serializer = propertyRegistry.getSerializer(property.getType());
          if (serializer == null) {
            logger.warning(
                "Unknown serializer for property '"
                    + key
                    + "' for npc '"
                    + rs.getString("npc_id")
                    + "'. skipping ...");
            continue;
          }
          Object value = serializer.deserialize(rs.getString("value"));
          if (value == null) {
            logger.warning(
                "Failed to deserialize property '"
                    + key
                    + "' for npc '"
                    + rs.getString("npc_id")
                    + "'. Resetting to default ...");
            value = property.getDefaultValue();
          }
          entry.getNpc().UNSAFE_setProperty(property, value);
          npcMap.put(rs.getString("npc_id"), entry);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try (PreparedStatement st =
            connection.prepareStatement(
                "SELECT * FROM " + TABLE_NPCS_HOLOGRAMS + " ORDER BY line");
        ResultSet rs = st.executeQuery()) {
      while (rs.next()) {
        NpcEntryImpl entry = npcMap.get(rs.getString("npc_id"));
        if (entry != null) {
          entry.getNpc().getHologram().insertLine(rs.getInt("line"), rs.getString("text"));
        }
        npcMap.put(rs.getString("npc_id"), entry);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try (PreparedStatement st =
            connection.prepareStatement(
                "SELECT * FROM " + TABLE_NPCS_ACTIONS + " ORDER BY action_id");
        ResultSet rs = st.executeQuery()) {
      while (rs.next()) {
        NpcEntryImpl entry = npcMap.get(rs.getString("npc_id"));
        if (entry != null) {
          entry.getNpc().addAction(actionRegistry.deserialize(rs.getString("action_data")));
        }
        npcMap.put(rs.getString("npc_id"), entry);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return npcMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  public void saveNpcs(Collection<NpcEntryImpl> npcs) {
    Connection connection = database.getSQLConnection();
    if (connection == null) {
      logger.severe("Failed to save npcs: no active SQL connection");
      return;
    }

    long start = System.currentTimeMillis();
    for (NpcEntryImpl entry : npcs) {
      boolean previousAutoCommit = true;
      try {
        previousAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        NpcImpl npc = entry.getNpc();
        HologramImpl hologram = npc.getHologram();

        try (PreparedStatement ps =
            connection.prepareStatement(
                "REPLACE INTO "
                    + TABLE_NPCS
                    + " (id, isProcessed, allowCommands, enabled, uuid, world, x, y, z, yaw, pitch, type, hologramOffset, hologramRefreshDelay) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
          ps.setString(1, entry.getId());
          ps.setBoolean(2, entry.isProcessed());
          ps.setBoolean(3, entry.isAllowCommandModification());
          ps.setBoolean(4, npc.isEnabled());
          ps.setString(5, npc.getUuid().toString());
          ps.setString(6, npc.getWorldName());
          ps.setDouble(7, npc.getLocation().getX());
          ps.setDouble(8, npc.getLocation().getY());
          ps.setDouble(9, npc.getLocation().getZ());
          ps.setFloat(10, npc.getLocation().getYaw());
          ps.setFloat(11, npc.getLocation().getPitch());
          ps.setString(12, npc.getType().getName());
          ps.setDouble(13, hologram.getOffset());
          ps.setBigDecimal(14, new BigDecimal(hologram.getRefreshDelay()));
          ps.executeUpdate();
        }

        try (PreparedStatement ps =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_PROPERTIES + " WHERE npc_id = ?")) {
          ps.setString(1, entry.getId());
          ps.executeUpdate();
        }

        for (EntityProperty<?> property : npc.getAllProperties()) {
          try {
            PropertySerializer<?> serializer =
                propertyRegistry.getSerializer(((EntityPropertyImpl<?>) property).getType());
            if (serializer == null) {
              logger.warning(
                  "Unknown serializer for property '"
                      + property.getName()
                      + "' for npc '"
                      + entry.getId()
                      + "'. skipping ...");
              continue;
            }
            try (PreparedStatement ps =
                connection.prepareStatement(
                    "REPLACE INTO "
                        + TABLE_NPCS_PROPERTIES
                        + " (npc_id, property, value) VALUES(?,?,?)")) {
              ps.setString(1, entry.getId());
              ps.setString(2, property.getName());
              ps.setString(3, serializer.UNSAFE_serialize(npc.getProperty(property)));
              ps.executeUpdate();
            }
          } catch (Exception exception) {
            logger.severe(
                "Failed to serialize property "
                    + property.getName()
                    + " for npc with id "
                    + entry.getId());
            exception.printStackTrace();
          }
        }

        try (PreparedStatement ps =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_HOLOGRAMS + " WHERE npc_id = ? AND line > ?")) {
          ps.setString(1, entry.getId());
          ps.setInt(2, hologram.getLines().size() - 1);
          ps.executeUpdate();
        }

        for (int i = 0; i < hologram.getLines().size(); i++) {
          try (PreparedStatement ps =
              connection.prepareStatement(
                  "REPLACE INTO " + TABLE_NPCS_HOLOGRAMS + " (npc_id, line, text) VALUES(?,?,?)")) {
            ps.setString(1, entry.getId());
            ps.setInt(2, i);
            ps.setString(3, hologram.getLine(i));
            ps.executeUpdate();
          }
        }

        try (PreparedStatement ps =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_ACTIONS + " WHERE npc_id = ? AND action_id > ?")) {
          ps.setString(1, entry.getId());
          ps.setInt(2, npc.getActions().size() - 1);
          ps.executeUpdate();
        }

        for (int i = 0; i < npc.getActions().size(); i++) {
          String action = actionRegistry.serialize(npc.getActions().get(i));
          if (action == null) continue;
          try (PreparedStatement ps =
              connection.prepareStatement(
                  "REPLACE INTO "
                      + TABLE_NPCS_ACTIONS
                      + " (npc_id, action_id, action_data) VALUES(?,?,?)")) {
            ps.setString(1, entry.getId());
            ps.setInt(2, i);
            ps.setString(3, action);
            ps.executeUpdate();
          }
        }

        connection.commit();
      } catch (SQLException exception) {
        try {
          connection.rollback();
        } catch (SQLException rollbackException) {
          logger.severe("Failed to rollback transaction for npc with id " + entry.getId());
          rollbackException.printStackTrace();
        }
        logger.severe("Failed to save npc with id " + entry.getId());
        exception.printStackTrace();
      } finally {
        try {
          connection.setAutoCommit(previousAutoCommit);
        } catch (SQLException ignored) {
        }
      }
    }
    if (configManager.getConfig().debugEnabled()) {
      logger.info(
          "Saved " + npcs.size() + " npcs in " + (System.currentTimeMillis() - start) + "ms");
    }
  }

  @Override
  public void deleteNpc(NpcEntryImpl entry) {
    Connection connection = database.getSQLConnection();
    if (connection == null) return;

    try (PreparedStatement deleteNpc =
            connection.prepareStatement("DELETE FROM " + TABLE_NPCS + " WHERE id = ?");
        PreparedStatement deleteProperties =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_PROPERTIES + " WHERE npc_id = ?");
        PreparedStatement deleteHolograms =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_HOLOGRAMS + " WHERE npc_id = ?");
        PreparedStatement deleteActions =
            connection.prepareStatement(
                "DELETE FROM " + TABLE_NPCS_ACTIONS + " WHERE npc_id = ?")) {

      deleteNpc.setString(1, entry.getId());
      deleteNpc.executeUpdate();

      deleteProperties.setString(1, entry.getId());
      deleteProperties.executeUpdate();

      deleteHolograms.setString(1, entry.getId());
      deleteHolograms.executeUpdate();

      deleteActions.setString(1, entry.getId());
      deleteActions.executeUpdate();
    } catch (SQLException exception) {
      logger.severe("Failed to delete npc with id " + entry.getId());
      exception.printStackTrace();
    }
  }

  @Override
  public void close() {
    database.close();
  }
}
