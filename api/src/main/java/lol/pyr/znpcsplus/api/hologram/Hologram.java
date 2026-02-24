package lol.pyr.znpcsplus.api.hologram;

/** Represents a hologram */
public interface Hologram {
  /**
   * Adds a line to the hologram Note: to add an item line, pass "item:&#60;item&#62;" as the line
   *
   * @param line The line to add
   */
  void addLine(String line);

  /**
   * Gets a line from the hologram
   *
   * @param index The index of the line to get
   * @return The line at the index
   */
  String getLine(int index);

  /**
   * Removes a line from the hologram
   *
   * @param index The index of the line to remove
   */
  void removeLine(int index);

  /** Clears all lines from the hologram */
  void clearLines();

  /**
   * Inserts a line into the hologram
   *
   * @param index The index to insert the line at
   * @param line The line to insert
   */
  void insertLine(int index, String line);

  /**
   * Gets the number of lines in the hologram
   *
   * @return The number of lines in the hologram
   */
  int lineCount();

  /**
   * Gets the refresh delay of the hologram
   *
   * @return The refresh delay of the hologram
   */
  long getRefreshDelay();

  /**
   * Sets the refresh delay of the hologram
   *
   * @param delay The delay to set
   */
  void setRefreshDelay(long delay);
}
