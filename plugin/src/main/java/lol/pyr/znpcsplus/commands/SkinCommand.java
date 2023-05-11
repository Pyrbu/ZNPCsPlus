package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class SkinCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " skin <id> <type> [value]");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        if (npc.getType() != NpcTypeImpl.byName("player")) context.halt(Component.text("The NPC must be a player to have a skin", NamedTextColor.RED));
        String type = context.popString();

        if (type.equalsIgnoreCase("mirror")) {
            npc.setProperty(EntityPropertyImpl.SKIN, new MirrorDescriptor());
            npc.respawn();
            context.halt(Component.text("The NPC's skin will now mirror the player that it's being displayed to", NamedTextColor.GREEN));
        }

        if (type.equalsIgnoreCase("static")) {
            context.ensureArgsNotEmpty();
            String name = context.dumpAllArgs();
            context.send(Component.text("Fetching skin \"" + name + "\"...", NamedTextColor.GREEN));
            PrefetchedDescriptor.forPlayer(name).thenAccept(skin -> {
                if (skin == null) {
                    context.send(Component.text("Failed to fetch skin, are you sure the player name is valid?", NamedTextColor.RED));
                    return;
                }
                npc.setProperty(EntityPropertyImpl.SKIN, skin);
                npc.respawn();
                context.send(Component.text("The NPC's skin has been set to \"" + name + "\""));
            });
            return;
        }

        if (type.equalsIgnoreCase("dynamic")) {
            context.ensureArgsNotEmpty();
            String name = context.dumpAllArgs();
            npc.setProperty(EntityPropertyImpl.SKIN, new FetchingDescriptor(name));
            npc.respawn();
            context.halt(Component.text("The NPC's skin will now be resolved per-player from \"" + name + "\""));
        }
        context.send(Component.text("Unknown skin type! Please use one of the following: mirror, static, dynamic"));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(NpcRegistryImpl.get().modifiableIds());
        if (context.argSize() == 2) return context.suggestLiteral("mirror", "static", "dynamic");
        if (context.matchSuggestion("*", "static")) return context.suggestPlayers();
        return Collections.emptyList();
    }
}
