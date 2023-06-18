package lol.pyr.znpcsplus.commands.storage;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.conversion.DataImporter;
import lol.pyr.znpcsplus.conversion.DataImporterRegistry;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ImportCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;
    private final DataImporterRegistry importerRegistry;

    public ImportCommand(NpcRegistryImpl npcRegistry, DataImporterRegistry importerRegistry) {
        this.npcRegistry = npcRegistry;
        this.importerRegistry = importerRegistry;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " storage import <importer>");
        String id = context.popString().toUpperCase();
        DataImporter importer = importerRegistry.getImporter(id);
        if (importer == null) context.halt(Component.text("Importer not found! Possible importers: " +
                String.join(", ", importerRegistry.getIds()), NamedTextColor.RED));

        CompletableFuture.runAsync(() -> {
            if (!importer.isValid()) {
                context.send(Component.text("There is no data to import from this importer!", NamedTextColor.RED));
                return;
            }
            try {
                Collection<NpcEntryImpl> entries = importer.importData();
                npcRegistry.registerAll(entries);
                context.send(Component.text(entries.size() + " npcs have been loaded from " + id, NamedTextColor.GREEN));
            } catch (Exception exception) {
                context.send(Component.text("Importing failed! Please check the console for more details.", NamedTextColor.RED));
                exception.printStackTrace();
            }
        });
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(importerRegistry.getIds());
        return Collections.emptyList();
    }
}
