package lol.pyr.znpcsplus.util;

public enum WoldVariant {
  PALE(3),
  SPOTTED(6),
  SNOWY(5),
  BLACK(1),
  ASHEN(0),
  RUSTY(4),
  WOODS(8),
  CHESTNUT(2),
  STRIPED(7);

  private final int id;

  WoldVariant(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
