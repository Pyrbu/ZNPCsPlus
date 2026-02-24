package lol.pyr.znpcsplus.util;

public enum SkeletonType {
  NORMAL,
  WITHER,
  STRAY;

  public byte getLegacyId() {
    return (byte) ordinal();
  }
}
