package lol.pyr.znpcsplus.interaction.playercommand;

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

public class PlayerCommandActionType implements InteractionActionType<PlayerCommandAction>, InteractionCommandHandler {
    private final TaskScheduler scheduler;
    private final NpcRegistryImpl npcRegistry;

    public PlayerCommandActionType(TaskScheduler scheduler, NpcRegistryImpl npcRegistry) {
        this.scheduler = scheduler;
        this.npcRegistry = npcRegistry;
    }

    @Override
    public String serialize(PlayerCommandAction obj) {
        return Base64.getEncoder().encodeToString(obj.getCommand().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public PlayerCommandAction deserialize(String str) {
        String[] split = str.split(";");
        return new PlayerCommandAction(scheduler, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }

    @Override
    public Class<PlayerCommandAction> getActionClass() {
        return PlayerCommandAction.class;
    }

    @Override
    public String getSubcommandName() {
        return "playercommand";
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getUsage() + " playercommand <id> <cooldown seconds> <command>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        long cooldown = (long) (context.parse(Double.class) * 1000D);
        String command = context.dumpAllArgs();
        entry.getNpc().addAction(new PlayerCommandAction(scheduler, command, cooldown));
        context.send(Component.text("Added a player command action to the npc with the command " + command, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestLiteral("1");
        return Collections.emptyList();
    }
}
