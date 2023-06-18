package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class TypeCommand implements CommandHandler {
    private final NpcRegistryImpl registry;
    private final NpcTypeRegistryImpl typeRegistry;

    public TypeCommand(NpcRegistryImpl registry, NpcTypeRegistryImpl typeRegistry) {
        this.registry = registry;
        this.typeRegistry = typeRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " type <id> <type>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        NpcTypeImpl type = context.parse(NpcTypeImpl.class);
        npc.setType(type);
        context.send(Component.text("NPC type set to " + type.getName() + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(registry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(typeRegistry.getAllImpl().stream().map(NpcTypeImpl::getName));
        return Collections.emptyList();
    }
}
