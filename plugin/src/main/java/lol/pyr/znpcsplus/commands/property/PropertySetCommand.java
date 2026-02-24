package lol.pyr.znpcsplus.commands.property;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.properties.attributes.AttributeProperty;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PropertySetCommand implements CommandHandler {
  private final NpcRegistryImpl npcRegistry;

  public PropertySetCommand(NpcRegistryImpl npcRegistry) {
    this.npcRegistry = npcRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    context.setUsage(context.getLabel() + " property set <id> <property> <value>");
    NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
    NpcImpl npc = entry.getNpc();
    EntityPropertyImpl<?> property = context.parse(EntityPropertyImpl.class);
    ensurePropertyCanBeSet(npc, property, context);

    ParsedValue parsedValue = parseValue(context, npc, property);
    if (parsedValue == null) return;

    Class<?> type = property.getType();
    npc.UNSAFE_setProperty(property, parsedValue.value);
    if (type == Component.class && parsedValue.value != null) {
      context.send(
          Component.text(
                  "Set property " + property.getName() + " for NPC " + entry.getId() + " to ",
                  NamedTextColor.GREEN)
              .append((Component) parsedValue.value));
    } else {
      context.send(
          Component.text(
              "Set property "
                  + property.getName()
                  + " for NPC "
                  + entry.getId()
                  + " to "
                  + parsedValue.valueName,
              NamedTextColor.GREEN));
    }
  }

  private void ensurePropertyCanBeSet(
      NpcImpl npc, EntityPropertyImpl<?> property, CommandContext context)
      throws CommandExecutionException {
    if (!npc.getType().getAllowedProperties().contains(property)) {
      context.halt(
          Component.text(
              "Property "
                  + property.getName()
                  + " not allowed for npc type "
                  + npc.getType().getName(),
              NamedTextColor.RED));
    }
    if (!property.isPlayerModifiable()) {
      context.halt(
          Component.text("This property is not modifiable by players", NamedTextColor.RED));
    }
  }

  private ParsedValue parseValue(
      CommandContext context, NpcImpl npc, EntityPropertyImpl<?> property)
      throws CommandExecutionException {
    Class<?> type = property.getType();
    if (type == ItemStack.class) return parseItemInHand(context);
    if (type == NamedColor.class && context.argSize() < 1 && npc.getProperty(property) != null)
      return new ParsedValue(null, "NONE");
    if (type == Color.class && context.argSize() < 1 && npc.getProperty(property) != null)
      return new ParsedValue(Color.BLACK, "NONE");
    if (type == ParrotVariant.class && context.argSize() < 1 && npc.getProperty(property) != null)
      return new ParsedValue(null, "NONE");
    if (type == BlockState.class) return parseBlockStateValue(context);
    if (type == SpellType.class) return parseSpellType(context);
    if (type == NpcEntryImpl.class) {
      NpcEntryImpl value = context.parse(NpcEntryImpl.class);
      return new ParsedValue(value, value == null ? "NONE" : value.getId());
    }
    if (type == Vector3i.class) {
      Vector3i value = context.parse(Vector3i.class);
      return new ParsedValue(value, value == null ? "NONE" : value.toPrettyString());
    }
    if (property instanceof AttributeProperty)
      return parseAttributeValue(context, (AttributeProperty) property);
    return parseDefaultValue(context, type);
  }

  private ParsedValue parseItemInHand(CommandContext context) throws CommandExecutionException {
    org.bukkit.inventory.ItemStack bukkitStack =
        context.ensureSenderIsPlayer().getInventory().getItemInHand();
    if (bukkitStack.getAmount() == 0) return new ParsedValue(null, "EMPTY");
    return new ParsedValue(
        SpigotConversionUtil.fromBukkitItemStack(bukkitStack), bukkitStack.toString());
  }

  private ParsedValue parseBlockStateValue(CommandContext context)
      throws CommandExecutionException {
    String inputType = context.popString().toLowerCase();
    switch (inputType) {
      case "hand":
        org.bukkit.inventory.ItemStack bukkitStack =
            context.ensureSenderIsPlayer().getInventory().getItemInHand();
        if (bukkitStack.getAmount() == 0) return new ParsedValue(new BlockState(0), "EMPTY");
        WrappedBlockState handBlockState =
            StateTypes.getByName(bukkitStack.getType().name().toLowerCase()).createBlockState();
        return new ParsedValue(
            new BlockState(handBlockState.getGlobalId()), bukkitStack.toString());
      case "looking_at":
        return new ParsedValue(new BlockState(0), "EMPTY");
      case "block":
        context.ensureArgsNotEmpty();
        WrappedBlockState blockState = WrappedBlockState.getByString(context.popString());
        return new ParsedValue(new BlockState(blockState.getGlobalId()), blockState.toString());
      default:
        context.send(
            Component.text(
                "Invalid input type " + inputType + ", must be hand, looking_at, or block",
                NamedTextColor.RED));
        return null;
    }
  }

  private ParsedValue parseSpellType(CommandContext context) throws CommandExecutionException {
    SpellType value = context.parse(SpellType.class);
    String valueName = String.valueOf(value);
    if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_13)
        && value.ordinal() > 3) {
      context.send(
          Component.text(
              "Spell type " + valueName + " is not supported on this version", NamedTextColor.RED));
      return null;
    }
    return new ParsedValue(value, valueName);
  }

  private ParsedValue parseAttributeValue(CommandContext context, AttributeProperty property)
      throws CommandExecutionException {
    Double value = context.parse(Double.class);
    if (value < property.getMinValue() || value > property.getMaxValue()) {
      double sanitizedValue = property.sanitizeValue(value);
      context.send(
          Component.text(
              "WARNING: Value "
                  + value
                  + " is out of range for property "
                  + property.getName()
                  + ", setting to "
                  + sanitizedValue,
              NamedTextColor.YELLOW));
      value = sanitizedValue;
    }
    return new ParsedValue(value, String.valueOf(value));
  }

  private ParsedValue parseDefaultValue(CommandContext context, Class<?> type)
      throws CommandExecutionException {
    try {
      Object value = context.parse(type);
      return new ParsedValue(value, String.valueOf(value));
    } catch (NullPointerException e) {
      context.send(
          Component.text(
              "An error occurred while trying to parse the value. Please report this to the plugin author.",
              NamedTextColor.RED));
      e.printStackTrace();
      return null;
    }
  }

  private static final class ParsedValue {
    private final Object value;
    private final String valueName;

    private ParsedValue(Object value, String valueName) {
      this.value = value;
      this.valueName = valueName;
    }
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
    if (context.argSize() == 2)
      return context.suggestStream(
          context
              .suggestionParse(0, NpcEntryImpl.class)
              .getNpc()
              .getType()
              .getAllowedProperties()
              .stream()
              .map(EntityProperty::getName));
    if (context.argSize() >= 3) {
      EntityPropertyImpl<?> property = context.suggestionParse(1, EntityPropertyImpl.class);
      Class<?> type = property.getType();
      if (type == Vector3f.class && context.argSize() <= 5)
        return context.suggestLiteral("0", "0.0");
      if (context.argSize() == 3) {
        if (type == Boolean.class) return context.suggestLiteral("true", "false");
        if (type == NamedColor.class) return context.suggestEnum(NamedColor.values());
        if (type == Color.class) return context.suggestLiteral("0x0F00FF", "#FFFFFF");
        if (type == BlockState.class) return context.suggestLiteral("hand", "looking_at", "block");
        if (type == SpellType.class)
          return PacketEvents.getAPI()
                  .getServerManager()
                  .getVersion()
                  .isOlderThan(ServerVersion.V_1_13)
              ? context.suggestEnum(
                  Arrays.stream(SpellType.values())
                      .filter(spellType -> spellType.ordinal() <= 3)
                      .toArray(SpellType[]::new))
              : context.suggestEnum(SpellType.values());

        if (type == Vector3i.class) {
          return suggestVector3i(context, 3);
        }
        // Suggest enum values directly
        if (type.isEnum()) {
          return context.suggestEnum((Enum<?>[]) type.getEnumConstants());
        }
      } else if (context.argSize() == 4) {
        if (type == BlockState.class) {
          // TODO: suggest block with nbt like minecraft setblock command
          return context.suggestionParse(2, String.class).equals("block")
              ? context.suggestStream(StateTypes.values().stream().map(StateType::getName))
              : Collections.emptyList();
        }
        if (type == Vector3i.class) {
          return suggestVector3i(context, 4);
        }
      } else if (context.argSize() == 5) {
        if (type == Vector3i.class) {
          return suggestVector3i(context, 5);
        }
      }
    }
    return Collections.emptyList();
  }

  private List<String> suggestVector3i(CommandContext context, int argSize)
      throws CommandExecutionException {
    if (!(context.getSender() instanceof Player)) return Collections.emptyList();
    Player player = (Player) context.getSender();
    Block targetBlock = player.getTargetBlock(Collections.singleton(Material.AIR), 5);
    if (targetBlock.getType().equals(Material.AIR)) return Collections.emptyList();
    if (argSize == 3) {
      return context.suggestLiteral(
          targetBlock.getX() + "",
          targetBlock.getX() + " " + targetBlock.getY(),
          targetBlock.getX() + " " + targetBlock.getY() + " " + targetBlock.getZ());
    }
    if (argSize == 4) {
      return context.suggestLiteral(
          targetBlock.getY() + "", targetBlock.getY() + " " + targetBlock.getZ());
    }
    if (argSize == 5) {
      return context.suggestLiteral(targetBlock.getZ() + "");
    }
    return Collections.emptyList();
  }
}
