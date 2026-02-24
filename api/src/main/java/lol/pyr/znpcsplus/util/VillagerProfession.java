package lol.pyr.znpcsplus.util;

public enum VillagerProfession {
  NONE(0, 0),
  ARMORER(1, 3),
  BUTCHER(2, 4),
  CARTOGRAPHER(3, 1),
  CLERIC(4, 2),
  FARMER(5, 0),
  FISHERMAN(6, 0),
  FLETCHER(7, 0),
  LEATHER_WORKER(8, 4),
  LIBRARIAN(9, 1),
  MASON(10),
  NITWIT(11, 5),
  SHEPHERD(12, 0),
  TOOL_SMITH(13, 3),
  WEAPON_SMITH(14, 3);

  private final int id;
  private final int legacyId;

  VillagerProfession(int id) {
    this.id = id;
    this.legacyId = 0;
  }

  VillagerProfession(int id, int legacyId) {
    this.id = id;
    this.legacyId = legacyId;
  }

  public int getId() {
    return id;
  }

  public int getLegacyId() {
    return legacyId;
  }
}
