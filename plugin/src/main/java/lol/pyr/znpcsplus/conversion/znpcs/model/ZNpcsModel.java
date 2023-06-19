package lol.pyr.znpcsplus.conversion.znpcs.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class ZNpcsModel {
    private int id;
    private UUID uuid;
    private double hologramHeight;
    private String skinName;
    private String glowName;
    private ZNpcsLocation location;
    private String npcType;
    private List<String> hologramLines;
    private List<ZNpcsAction> clickActions;
    private Map<String, String> npcEquip;
    private Map<String, String[]> customizationMap;

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getHologramHeight() {
        return hologramHeight;
    }

    public String getSkinName() {
        return skinName;
    }

    public ZNpcsLocation getLocation() {
        return location;
    }

    public String getNpcType() {
        return npcType;
    }

    public List<String> getHologramLines() {
        return hologramLines;
    }

    public List<ZNpcsAction> getClickActions() {
        return clickActions;
    }

    public Map<String, String> getNpcEquip() {
        return npcEquip;
    }

    public Map<String, String[]> getCustomizationMap() {
        return customizationMap;
    }
}