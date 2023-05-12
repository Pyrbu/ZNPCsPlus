package lol.pyr.znpcsplus.commands.storage;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SaveAllCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        CompletableFuture.runAsync(() -> {
            NpcRegistryImpl.get().save();
            context.send(Component.text("All NPCs have been saved to storage", NamedTextColor.GREEN));
        });
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        return Collections.emptyList();
    }
}
