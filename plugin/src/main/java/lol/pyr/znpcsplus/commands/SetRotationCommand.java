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

public class SetRotationCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public SetRotationCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " setrotation <id> <yaw> <pitch>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        float yaw = parseRotation(context.popString(), npc.getLocation().getYaw());
        float pitch = parseRotation(context.popString(), npc.getLocation().getPitch());
        if (pitch < -90 || pitch > 90) {
            pitch = Math.min(Math.max(pitch, -90), 90);
            context.send(Component.text("Warning: pitch is outside of the -90 to 90 range. It has been normalized to " + pitch + ".", NamedTextColor.YELLOW));
        }
        npc.setLocation(new NpcLocation(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), yaw, pitch));
        context.send(Component.text("NPC has been rotated to " + yaw + ", " + pitch + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        NpcImpl npc = context.suggestionParse(0, NpcEntryImpl.class).getNpc();
        if (context.argSize() == 2) return Arrays.asList(String.valueOf(npc.getLocation().getYaw()), "~");
        else if (context.argSize() == 3) return Arrays.asList(String.valueOf(npc.getLocation().getPitch()), "~");
        return Collections.emptyList();
    }

    private static float parseRotation(String input, float current) throws CommandExecutionException {
        if (input.equals("~")) return current;
        if (input.startsWith("~")) {
            try {
                return current + Float.parseFloat(input.substring(1));
            } catch (NumberFormatException e) {
                throw new CommandExecutionException();
            }
        }
        return Float.parseFloat(input);
    }
}
