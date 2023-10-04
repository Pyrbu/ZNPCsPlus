package lol.pyr.znpcsplus.commands.property;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class PropertyRemoveCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public PropertyRemoveCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " property remove <id> <property>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        NpcImpl npc = entry.getNpc();
        EntityPropertyImpl<?> property = context.parse(EntityPropertyImpl.class);
        if (!npc.hasProperty(property)) context.halt(Component.text("This npc doesn't have the " + property.getName() + " property set", NamedTextColor.RED));
        if (!property.isPlayerModifiable()) context.halt(Component.text("This property is not modifiable by players", NamedTextColor.RED));
        npc.setProperty(property, null);
        context.send(Component.text("Removed property " + property.getName() + " from NPC " + entry.getId(), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(context.suggestionParse(0, NpcEntryImpl.class)
                .getNpc().getAllProperties().stream().filter(EntityProperty::isPlayerModifiable).map(EntityProperty::getName));
        return Collections.emptyList();
    }
}
