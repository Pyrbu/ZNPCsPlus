package lol.pyr.znpcsplus.interaction.switchserver;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.InteractionActionType;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class SwitchServerActionType implements InteractionActionType<SwitchServerAction>, InteractionCommandHandler {
    private final BungeeConnector bungeeConnector;
    private final NpcRegistryImpl npcRegistry;

    public SwitchServerActionType(BungeeConnector bungeeConnector, NpcRegistryImpl npcRegistry) {
        this.bungeeConnector = bungeeConnector;
        this.npcRegistry = npcRegistry;
    }

    @Override
    public String serialize(SwitchServerAction obj) {
        return Base64.getEncoder().encodeToString(obj.getServer().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public SwitchServerAction deserialize(String str) {
        String[] split = str.split(";");
        return new SwitchServerAction(bungeeConnector, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }

    @Override
    public Class<SwitchServerAction> getActionClass() {
        return SwitchServerAction.class;
    }

    @Override
    public String getSubcommandName() {
        return "switchserver";
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getUsage() + " switchserver <id> <cooldown seconds> <server>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        long cooldown = (long) (context.parse(Double.class) * 1000D);
        String server = context.dumpAllArgs();
        entry.getNpc().addAction(new SwitchServerAction(bungeeConnector, server, cooldown));
        context.send(Component.text("Added a switch server action to the npc with the server " + server, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.modifiableIds());
        if (context.argSize() == 2) return context.suggestLiteral("1");
        return Collections.emptyList();
    }
}
