package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.StringCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.util.VillagerProfession;
import org.jetbrains.annotations.NotNull;

public class ProfessionTrait extends StringCitizensTrait {
    private final EntityPropertyRegistry registry;

    public ProfessionTrait(EntityPropertyRegistry registry) {
        super("profession");
        this.registry = registry;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, String string) {
        VillagerProfession profession;
        try {
            profession = VillagerProfession.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            profession = VillagerProfession.NONE;
        }
        npc.setProperty(registry.getByName("villager_profession", VillagerProfession.class), profession);
        return npc;
    }
}
