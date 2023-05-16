package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class PropertiesCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public PropertiesCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        NpcImpl npc = entry.getNpc();
        EntityPropertyImpl<?> property = context.parse(EntityPropertyImpl.class);

        if (!npc.getType().getAllowedProperties().contains(property)) context.halt(Component.text("Property " + property.getName() + " not allowed for npc type " + npc.getType().getName()));

        Object value = context.parse(property.getType());
        npc.UNSAFE_setProperty(property, value);
        context.send(Component.text("Set property " + property.getName() + " for NPC " + entry.getId() + " to " + value.toString(), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.modifiableIds());
        if (context.argSize() == 2) return context.suggestStream(context.suggestionParse(0, NpcEntryImpl.class)
                    .getNpc().getType().getAllowedProperties().stream().map(EntityPropertyImpl::getName));
        if (context.argSize() == 3) {
            EntityPropertyImpl<?> property = context.suggestionParse(1, EntityPropertyImpl.class);
            Class<?> type = property.getType();
            if (type == Boolean.class) return context.suggestLiteral("true", "false");
            if (type == NamedTextColor.class) return context.suggestCollection(NamedTextColor.NAMES.keys());
        }
        return Collections.emptyList();
    }
}
