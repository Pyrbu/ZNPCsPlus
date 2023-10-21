package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class SetRotationCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public SetRotationCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " setrotation <id> <yaw> <pitch>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        float yaw = context.parse(Float.class);
        float pitch = context.parse(Float.class);
        npc.setLocation(new NpcLocation(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), yaw, pitch));
        context.send(Component.text("NPC has been rotated to " + yaw + ", " + pitch + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
