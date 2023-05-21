package lol.pyr.znpcsplus.interaction.consolecommand;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.InteractionActionType;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ConsoleCommandActionType implements InteractionActionType<ConsoleCommandAction>, InteractionCommandHandler {
    private final TaskScheduler scheduler;
    private final NpcRegistryImpl npcRegistry;

    public ConsoleCommandActionType(TaskScheduler scheduler, NpcRegistryImpl npcRegistry) {
        this.scheduler = scheduler;
        this.npcRegistry = npcRegistry;
    }

    @Override
    public String serialize(ConsoleCommandAction obj) {
        return Base64.getEncoder().encodeToString(obj.getCommand().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public ConsoleCommandAction deserialize(String str) {
        String[] split = str.split(";");
        return new ConsoleCommandAction(scheduler, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }

    @Override
    public Class<ConsoleCommandAction> getActionClass() {
        return ConsoleCommandAction.class;
    }

    @Override
    public String getSubcommandName() {
        return "consolecommand";
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getUsage() + " consolecommand <id> <cooldown seconds> <command>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        long cooldown = (long) (context.parse(Double.class) * 1000D);
        String command = context.dumpAllArgs();
        entry.getNpc().addAction(new ConsoleCommandAction(scheduler, command, cooldown));
        context.send(Component.text("Added a console command action to the npc with the command " + command, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.modifiableIds());
        if (context.argSize() == 2) return context.suggestLiteral("1");
        return Collections.emptyList();
    }
}
