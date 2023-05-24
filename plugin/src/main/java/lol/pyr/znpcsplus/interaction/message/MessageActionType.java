package lol.pyr.znpcsplus.interaction.message;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.InteractionActionType;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.interaction.InteractionType;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class MessageActionType implements InteractionActionType<MessageAction>, InteractionCommandHandler {
    private final BukkitAudiences adventure;
    private final LegacyComponentSerializer textSerializer;
    private final NpcRegistryImpl npcRegistry;

    public MessageActionType(BukkitAudiences adventure, LegacyComponentSerializer textSerializer, NpcRegistryImpl npcRegistry) {
        this.adventure = adventure;
        this.textSerializer = textSerializer;
        this.npcRegistry = npcRegistry;
    }

    @Override
    public String serialize(MessageAction obj) {
        return Base64.getEncoder().encodeToString(MiniMessage.miniMessage().serialize(obj.getMessage()).getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public MessageAction deserialize(String str) {
        String[] split = str.split(";");
        InteractionType type = split.length > 2 ? InteractionType.valueOf(split[2]) : InteractionType.ANY_CLICK;
        return new MessageAction(adventure, MiniMessage.miniMessage().deserialize(new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8)), type, Long.parseLong(split[1]));
    }

    @Override
    public Class<MessageAction> getActionClass() {
        return MessageAction.class;
    }

    @Override
    public String getSubcommandName() {
        return "message";
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getUsage() + " consolecommand <id> <type> <cooldown seconds> <message>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        InteractionType type = context.parse(InteractionType.class);
        long cooldown = (long) (context.parse(Double.class) * 1000D);
        Component message = textSerializer.deserialize(context.dumpAllArgs());
        entry.getNpc().addAction(new MessageAction(adventure, message, type, cooldown));
        context.send(Component.text("Added a message action to the npc with the message ", NamedTextColor.GREEN).append(message));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestEnum(InteractionType.values());
        if (context.argSize() == 3) return context.suggestLiteral("1");
        return Collections.emptyList();
    }
}
