package lol.pyr.znpcsplus.conversion.znpcs.model;

import java.util.List;
import java.util.Map;

public class ZNpcsModel {
    private int id;
    private double hologramHeight;
    private String skin;
    private String signature;
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

    public double getHologramHeight() {
        return hologramHeight;
    }

    public String getSkin() {
        return skin;
    }

    public String getSignature() {
        return signature;
    }

    public String getGlowName() {
        return glowName;
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