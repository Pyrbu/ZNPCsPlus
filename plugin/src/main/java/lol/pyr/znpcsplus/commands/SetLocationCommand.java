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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetLocationCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public SetLocationCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " setlocation <id> <x> <y> <z>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        double x = parseLocation(context.popString(), npc.getLocation().getX());
        double y = parseLocation(context.popString(), npc.getLocation().getY());
        double z = parseLocation(context.popString(), npc.getLocation().getZ());
        npc.setLocation(new NpcLocation(x, y, z, npc.getLocation().getYaw(), npc.getLocation().getPitch()));
        context.send(Component.text("NPC has been moved to " + x + ", " + y + ", " + z + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        NpcImpl npc = context.suggestionParse(0, NpcEntryImpl.class).getNpc();
        if (context.argSize() == 2) return Arrays.asList(String.valueOf(npc.getLocation().getX()), "~");
        else if (context.argSize() == 3) return Arrays.asList(String.valueOf(npc.getLocation().getY()), "~");
        else if (context.argSize() == 4) return Arrays.asList(String.valueOf(npc.getLocation().getZ()), "~");
        return Collections.emptyList();
    }

    private static double parseLocation(String input, double current) throws CommandExecutionException {
        if (input.equals("~")) return current;
        if (input.startsWith("~")) {
            try {
                return current + Double.parseDouble(input.substring(1));
            } catch (NumberFormatException e) {
                throw new CommandExecutionException();
            }
        }
        return Double.parseDouble(input);
    }
}
