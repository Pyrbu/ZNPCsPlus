package lol.pyr.znpcsplus.commands;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcPose;
import lol.pyr.znpcsplus.util.Vector3f;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;

import java.util.Collections;
import java.util.List;

public class PropertyCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public PropertyCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " property <id> <property> <value>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        NpcImpl npc = entry.getNpc();
        EntityPropertyImpl<?> property = context.parse(EntityPropertyImpl.class);

        // TODO: find a way to do this better & rewrite this mess

        if (!npc.getType().getAllowedProperties().contains(property)) context.halt(Component.text("Property " + property.getName() + " not allowed for npc type " + npc.getType().getName(), NamedTextColor.RED));
        Class<?> type = property.getType();
        Object value;
        String valueName;
        if (type == ItemStack.class) {
            org.bukkit.inventory.ItemStack bukkitStack = context.ensureSenderIsPlayer().getInventory().getItemInHand();
            if (bukkitStack.getAmount() == 0) {
                value = null;
                valueName = "EMPTY";
            } else {
                value = SpigotConversionUtil.fromBukkitItemStack(bukkitStack);
                valueName = bukkitStack.toString();
            }
        }
        else if (type == NamedTextColor.class && context.argSize() < 1 && npc.getProperty(property) != null) {
            value = null;
            valueName = "NONE";
        }
        else if (type == Color.class && context.argSize() < 1 && npc.getProperty(property) != null) {
            value = Color.BLACK;
            valueName = "NONE";
        }
        else {
            value = context.parse(type);
            valueName = String.valueOf(value);
        }

        npc.UNSAFE_setProperty(property, value);
        context.send(Component.text("Set property " + property.getName() + " for NPC " + entry.getId() + " to " + valueName, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(context.suggestionParse(0, NpcEntryImpl.class)
                    .getNpc().getType().getAllowedProperties().stream().map(EntityProperty::getName));
        if (context.argSize() >= 3) {
            EntityPropertyImpl<?> property = context.suggestionParse(1, EntityPropertyImpl.class);
            Class<?> type = property.getType();
            if (type == Vector3f.class && context.argSize() <= 5) return context.suggestLiteral("0", "0.0");
            if (context.argSize() == 3) {
                if (type == Boolean.class) return context.suggestLiteral("true", "false");
                if (type == NamedTextColor.class) return context.suggestCollection(NamedTextColor.NAMES.keys());
                if (type == NpcPose.class) return context.suggestEnum(NpcPose.values());
                if (type == Color.class) return context.suggestLiteral("0x0F00FF", "#FFFFFF", "16711935");
            }
        }
        return Collections.emptyList();
    }
}
