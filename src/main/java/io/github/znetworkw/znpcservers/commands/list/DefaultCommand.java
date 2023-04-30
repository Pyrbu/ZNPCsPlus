package io.github.znetworkw.znpcservers.commands.list;

import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import io.github.znetworkw.znpcservers.commands.CommandTabInformation;
import lol.pyr.znpcsplus.ZNPCsPlus;
import io.github.znetworkw.znpcservers.commands.Command;
import io.github.znetworkw.znpcservers.commands.CommandInformation;
import io.github.znetworkw.znpcservers.commands.CommandSender;
import io.github.znetworkw.znpcservers.commands.list.inventory.ConversationGUI;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.*;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class DefaultCommand extends Command {
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");

    private static final Joiner SPACE_JOINER = Joiner.on(" ");

    private static final SkinFunction DO_APPLY_SKIN = (sender, npc, skin) -> NPCSkin.forName(skin, (values, ex) -> {
        if (ex != null) {
            Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.CANT_GET_SKIN, skin);
            ZNPCsPlus.LOGGER.warning("Failed to fetch skin:");
            ex.printStackTrace();
            return;
        }
        npc.changeSkin(NPCSkin.forValues(values));
        Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.GET_SKIN);
    });

    public DefaultCommand() {
        super("znpcs");
    }

    @CommandInformation(arguments = {}, name = "", permission = "")
    public void defaultCommand(CommandSender sender, Map<String, String> args) {
        sender.sendMessage("&6&m------------------------------------------");
        sender.sendMessage("&b&lZNPCsPlus &8\u00BB &7ZNetwork & Pyr");
        sender.sendMessage("&6https://www.spigotmc.org/resources/znpcsplus.109380/");
        Objects.requireNonNull(sender);
        getCommands().forEach(sender::sendMessage);
        sender.sendMessage(ChatColor.DARK_GRAY + "Hover over the commands to view command arguments.");
        sender.sendMessage("&6&m------------------------------------------");
    }

    @CommandInformation(arguments = {"id", "type", "name"}, name = "create", permission = "znpcs.cmd.create", help = {" &f&l* &e/znpcs create <npc_id> PLAYER Steve"})
    public void createNPC(CommandSender sender, Map<String, String> args) {
        if (args.size() < 3) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        boolean foundNPC = ConfigurationConstants.NPC_LIST.stream().anyMatch(npc -> (npc.getId() == id));
        if (foundNPC) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_FOUND);
            return;
        }
        String name = args.get("name").trim();
        if (name.length() < 3 || name.length() > 16) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
            return;
        }
        NPCType npcType = NPCType.valueOf(args.get("type").toUpperCase());
        if (npcType.getConstructor() == null && !npcType.equals(NPCType.PLAYER)) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NOT_SUPPORTED_NPC_TYPE);
            return;
        }
        ZNPCsPlus.createNPC(id, npcType, sender.getPlayer().getLocation(), name);
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "create", permission = "znpcs.cmd.create")
    public List<String> createNPCTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("<npc_id>");
        }
        if (args.length == 2) {
            return partialTabCompleteOptions(args[1], Arrays.stream(NPCType.values()).map(Enum::toString).collect(Collectors.toList()));
        }
        if (args.length == 3) {
            return Lists.newArrayList("<name>");
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id"}, name = "delete", permission = "znpcs.cmd.delete", help = {" &f&l* &e/znpcs delete <npc_id>"})
    public void deleteNPC(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        ZNPCsPlus.deleteNPC(id);
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "delete", permission = "znpcs.cmd.delete")
    public List<String> deleteNPCTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {}, name = "list", permission = "znpcs.cmd.list")
    public void list(CommandSender sender, Map<String, String> args) {
        if (ConfigurationConstants.NPC_LIST.isEmpty()) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_NPC_FOUND);
        } else {
            sender.sendMessage(ChatColor.DARK_GREEN + "NPC list:");
            for (NPCModel npcModel : ConfigurationConstants.NPC_LIST)
                ZNPCsPlus.ADVENTURE.player(sender.getPlayer()).sendMessage(Component.text("-", NamedTextColor.GREEN)
                        .append(Component.text(" " + npcModel.getId(), NamedTextColor.GREEN))
                        .append(Component.text(" " + npcModel.getHologramLines().toString() +
                                " (" + npcModel.getLocation().getWorldName() + " " + (int) npcModel.getLocation().getX() + " " +
                                (int) npcModel.getLocation().getY() + " " + (int) npcModel.getLocation().getZ() + ") ", NamedTextColor.GREEN))
                        .append(Component.text("[TELEPORT]", NamedTextColor.DARK_GREEN, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand("/znpcs teleport " + npcModel.getId()))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to teleport to this NPC."))))
                        .append(Component.text(" [DELETE]", NamedTextColor.RED, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand("/znpcs delete " + npcModel.getId()))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to delete this NPC.")))));
        }
    }

    @CommandTabInformation(name = "list", permission = "znpcs.cmd.list")
    public List<String> listTab(CommandSender sender, String[] args) {
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "skin"}, name = "skin", permission = "znpcs.cmd.skin", help = {" &f&l* &e/znpcs skin <npc_id> Notch"})
    public void setSkin(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        String skin = args.get("skin");
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.FETCHING_SKIN, skin);
        DO_APPLY_SKIN.apply(sender.getPlayer(), foundNPC, args.get("skin"));
    }

    @CommandTabInformation(name = "skin", permission = "znpcs.cmd.skin")
    public List<String> skinTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        if (args.length == 2) {
            return Lists.newArrayList("player_name", "link_to_skin");
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "slot"}, name = "equip", permission = "znpcs.cmd.equip", help = {" &f&l* &e/znpcs equip <npc_id> [HAND/OFFHAND/HELMET/CHESTPLATE/LEGGINGS/BOOTS]", "&8(You need to have the item in your hand)."})
    public void equip(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        foundNPC.getNpcPojo().getNpcEquip().put(EquipmentSlot.valueOf(args.get("slot").toUpperCase()), sender.getPlayer().getInventory().getItemInHand());
        foundNPC.getPackets().flushCache("equipPackets");
        Objects.requireNonNull(foundNPC);
        foundNPC.getViewers().forEach(foundNPC::sendEquipPackets);
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "equip", permission = "znpcs.cmd.equip")
    public List<String> equipTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        if (args.length == 2) {
            return partialTabCompleteOptions(args[1], Arrays.stream(EquipmentSlot.values()).map(Enum::toString).collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "lines"}, name = "lines", permission = "znpcs.cmd.lines", help = {" &f&l* &e/znpcs lines <npc_id> First Second Third-Space"})
    public void changeLines(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        foundNPC.getNpcPojo().setHologramLines(Lists.reverse(SPACE_SPLITTER.splitToList(args.get("lines"))));
        foundNPC.getHologram().createHologram();
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "lines", permission = "znpcs.cmd.lines")
    public List<String> linesTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id"}, name = "move", permission = "znpcs.cmd.move", help = {" &f&l* &e/znpcs move <npc_id>"})
    public void move(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        foundNPC.getNpcPojo().setLocation(new ZLocation(sender.getPlayer().getLocation()));
        foundNPC.changeType(foundNPC.getNpcPojo().getNpcType());
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "move", permission = "znpcs.cmd.move")
    public List<String> moveTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "type"}, name = "type", permission = "znpcs.cmd.type", help = {" &f&l* &e/znpcs type <npc_id> ZOMBIE"})
    public void changeType(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        NPCType npcType = NPCType.valueOf(args.get("type").toUpperCase());
        if (npcType != NPCType.PLAYER && npcType.getConstructor() == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.UNSUPPORTED_ENTITY);
            return;
        }
        foundNPC.changeType(npcType);
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "type", permission = "znpcs.cmd.type")
    public List<String> typeTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        if (args.length == 2) {
            return partialTabCompleteOptions(args[1], Arrays.stream(NPCType.values()).map(Enum::toString).collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"add", "remove", "cooldown", "list"}, name = "action", isMultiple = true, permission = "znpcs.cmd.action", help = {" &f&l* &e/znpcs action add <npc_id> SERVER survival", " &f&l* &e/znpcs action add <npc_id> CMD spawn", " &f&l* &e/znpcs action remove <npc_id> <action_id>", " &f&l* &e/znpcs action cooldown <npc_id> <action_id> <delay_in_seconds>", " &f&l* &e/znpcs action list <npc_id>"})
    public void action(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        if (args.containsKey("add")) {
            List<String> split = SPACE_SPLITTER.splitToList(args.get("add"));
            if (split.size() < 3) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.ACTION_ADD_INCORRECT_USAGE);
                return;
            }
            Integer id = Ints.tryParse(split.get(0));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            foundNPC.getNpcPojo().getClickActions().add(new NPCAction(split.get(1).toUpperCase(), SPACE_JOINER.join(Iterables.skip(split, 2))));
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
        } else if (args.containsKey("remove")) {
            List<String> split = SPACE_SPLITTER.splitToList(args.get("remove"));
            Integer id = Ints.tryParse(split.get(0));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            Integer actionId = Ints.tryParse(split.get(1));
            if (actionId == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            } else {
                if (actionId >= foundNPC.getNpcPojo().getClickActions().size()) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
                    return;
                }
                foundNPC.getNpcPojo().getClickActions().remove(actionId.intValue());
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            }
        } else if (args.containsKey("cooldown")) {
            List<String> split = SPACE_SPLITTER.splitToList(args.get("cooldown"));
            if (split.size() < 2) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.ACTION_DELAY_INCORRECT_USAGE);
                return;
            }
            Integer id = Ints.tryParse(split.get(0));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            Integer actionId = Ints.tryParse(split.get(1));
            Integer actionDelay = Ints.tryParse(split.get(2));
            if (actionId == null || actionDelay == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            } else {
                if (actionId >= foundNPC.getNpcPojo().getClickActions().size()) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
                    return;
                }
                foundNPC.getNpcPojo().getClickActions().get(actionId).setDelay(actionDelay);
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            }
        } else if (args.containsKey("list")) {
            Integer id = Ints.tryParse(args.get("list"));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            if (foundNPC.getNpcPojo().getClickActions().isEmpty()) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
            } else {
                foundNPC.getNpcPojo().getClickActions().forEach(s -> sender.sendMessage("&8(&a" + foundNPC.getNpcPojo().getClickActions().indexOf(s) + "&8) &6" + s.toString()));
            }
        }
    }

    @CommandTabInformation(name = "action", permission = "znpcs.cmd.action")
    public List<String> actionTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Lists.newArrayList("add", "remove", "cooldown", "list");
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], Lists.newArrayList("add", "remove", "cooldown", "list"));
        }

        switch (args[0].toLowerCase()) {
            default -> {
                return Lists.newArrayList();
            }
            case "add" -> {
                if (args.length == 2) {
                    return Lists.newArrayList("<npc_id>");
                }
                if (args.length == 3) {
                    return partialTabCompleteOptions(args[2], Arrays.stream(NPCAction.ActionType.values()).map(Enum::toString).collect(Collectors.toList()));
                }
                if (args.length == 4) {
                    NPCAction.ActionType actionType = NPCAction.ActionType.valueOf(args[2].toUpperCase());
                    switch (actionType) {
                        default -> {
                            return Lists.newArrayList();
                        }
                        case MESSAGE -> {
                            return Lists.newArrayList("<message>");
                        }
                        case CMD -> {
                            return Lists.newArrayList("<command>");
                        }
                        case CONSOLE -> {
                            return Lists.newArrayList("<console command>");
                        }
                        case CHAT -> {
                            return Lists.newArrayList("<chat>");
                        }
                        case SERVER -> {
                            return Lists.newArrayList("<server>");
                        }
                    }
                }
            }
            case "remove" -> {
                if (args.length == 2) {
                    return Lists.newArrayList("<npc_id>");
                }
                if (args.length == 3) {
                    return Lists.newArrayList("<action_id>");
                }
            }
            case "cooldown" -> {
                if (args.length == 2) {
                    return Lists.newArrayList("<npc_id>");
                }
                if (args.length == 3) {
                    return Lists.newArrayList("<action_id>");
                }
                if (args.length == 4) {
                    return Lists.newArrayList("<delay_in_seconds>");
                }
            }
            case "list" -> {
                if (args.length == 2) {
                    return Lists.newArrayList("<npc_id>");
                }
            }
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "type", "value"}, name = "toggle", permission = "znpcs.cmd.toggle", help = {" &f&l* &e/znpcs toggle <npc_id> look"})
    public void toggle(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        NPCFunction npcFunction = FunctionFactory.findFunctionForName(args.get("type"));
        if (npcFunction.getName().equalsIgnoreCase("glow")) {
            npcFunction.doRunFunction(foundNPC, new FunctionContext.ContextWithValue(foundNPC, args.get("value") != null ? args.get("value").toUpperCase() : "WHITE"));
        } else {
            npcFunction.doRunFunction(foundNPC, new FunctionContext.DefaultContext(foundNPC));
        }
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "toggle", permission = "znpcs.cmd.toggle")
    public List<String> toggleTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Lists.newArrayList("<npc_id>");
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], Lists.newArrayList("look", "glow"));
        }
        if (args.length == 2) {
            NPCFunction npcFunction = FunctionFactory.findFunctionForName(args[0]);
            if (npcFunction.getName().equalsIgnoreCase("glow")) {
                return partialTabCompleteOptions(args[1], Arrays.stream(org.bukkit.ChatColor.values()).filter(org.bukkit.ChatColor::isColor).map(Enum::toString).collect(Collectors.toList()));
            }
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "customizeValues"}, name = "customize", permission = "znpcs.cmd.customize", help = {" &f&l* &e/znpcs customize <npc_id> <customization>"})
    public void customize(CommandSender sender, Map<String, String> args) {
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        NPCType npcType = foundNPC.getNpcPojo().getNpcType();
        if (args.get("customizeValues") == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            for (Map.Entry<String, Method> method : npcType.getCustomizationLoader().getMethods().entrySet())
                sender.sendMessage(ChatColor.YELLOW + method.getKey() + " " + SPACE_JOINER.join(method.getValue().getParameterTypes()));
            return;
        }
        List<String> customizeOptions = SPACE_SPLITTER.splitToList(args.get("customizeValues"));
        String methodName = customizeOptions.get(0);
        if (npcType.getCustomizationLoader().contains(methodName)) {
            Method method = npcType.getCustomizationLoader().getMethods().get(methodName);
            Iterable<String> split = Iterables.skip(customizeOptions, 1);
            if (Iterables.size(split) < (method.getParameterTypes()).length) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.TOO_FEW_ARGUMENTS);
                return;
            }
            split = Iterables.transform(split, String::toUpperCase);
            String[] values = Iterables.toArray(split, String.class);
            try {
                npcType.updateCustomization(foundNPC, methodName, values);
                foundNPC.getNpcPojo().getCustomizationMap().put(methodName, values);
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            }catch (Exception e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_CUSTOMIZE_ARGUMENTS);
                if (ConfigurationConstants.DEBUG_ENABLED) {
                    e.printStackTrace();
                }
            }
        } else {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.METHOD_NOT_FOUND);
            for (Map.Entry<String, Method> method : npcType.getCustomizationLoader().getMethods().entrySet())
                sender.sendMessage(ChatColor.YELLOW + method.getKey() + " " + SPACE_JOINER.join(method.getValue().getParameterTypes()));
        }
    }

    @CommandTabInformation(name = "customize", permission = "znpcs.cmd.customize")
    public List<String> customizeTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Lists.newArrayList("<npc_id>");
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        if (args.length == 2) {
            NPC foundNPC = NPC.find(Integer.parseInt(args[1]));
            if (foundNPC == null)
                return Lists.newArrayList();
            NPCType npcType = foundNPC.getNpcPojo().getNpcType();
            return partialTabCompleteOptions(args[1], npcType.getCustomizationLoader().getMethods().keySet().stream().toList());
        }
        if (args.length == 3) {
            NPC foundNPC = NPC.find(Integer.parseInt(args[1]));
            if (foundNPC == null)
                return Lists.newArrayList();
            NPCType npcType = foundNPC.getNpcPojo().getNpcType();
            if (npcType.getCustomizationLoader().contains(args[2])) {
                Method method = npcType.getCustomizationLoader().getMethods().get(args[2]);
                return partialTabCompleteOptions(args[2], Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.toList()));
            }
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"set", "create", "exit", "path", "list"}, name = "path", isMultiple = true, permission = "znpcs.cmd.path", help = {" &f&l* &e/znpcs path create name", " &f&l* &e/znpcs path set <npc_id> name"})
    public void path(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        ZUser znpcUser = ZUser.find(sender.getPlayer());
        if (znpcUser == null)
            return;
        if (args.containsKey("set")) {
            List<String> split = SPACE_SPLITTER.splitToList(args.get("set"));
            if (split.size() < 2) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_SET_INCORRECT_USAGE);
                return;
            }
            Integer id = Ints.tryParse(split.get(0));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            foundNPC.setPath(NPCPath.AbstractTypeWriter.find(split.get(1)));
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
        } else if (args.containsKey("create")) {
            String pathName = args.get("create");
            if (pathName.length() < 3 || pathName.length() > 16) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
                return;
            }
            if (NPCPath.AbstractTypeWriter.find(pathName) != null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_FOUND);
                return;
            }
            if (znpcUser.isHasPath()) {
                sender.getPlayer().sendMessage(ChatColor.RED + "You already have a path creator active! To remove it, type " + ChatColor.WHITE + "/znpcs path exit" + ChatColor.RED + ".");
                return;
            }
            NPCPath.AbstractTypeWriter.forCreation(pathName, znpcUser, NPCPath.AbstractTypeWriter.TypeWriter.MOVEMENT);
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_START);
        } else if (args.containsKey("exit")) {
            znpcUser.setHasPath(false);
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.EXIT_PATH);
        } else if (args.containsKey("list")) {
            if (NPCPath.AbstractTypeWriter.getPaths().isEmpty()) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_PATH_FOUND);
            } else {
                NPCPath.AbstractTypeWriter.getPaths().forEach(path -> sender.getPlayer().sendMessage(ChatColor.GREEN + path.getName()));
            }
        }
    }

    @CommandTabInformation(name = "path" , permission = "znpcs.cmd.path")
    public List<String> pathTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Lists.newArrayList("set", "create", "exit", "list");
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], Lists.newArrayList("set", "create", "exit", "list"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                return partialTabCompleteOptions(args[1], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
            }
            if (args[0].equalsIgnoreCase("create")) {
                return Lists.newArrayList("<name>");
            }
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id"}, name = "teleport", permission = "znpcs.cmd.teleport", help = {" &f&l* &e/znpcs teleport <npc_id>"})
    public void teleport(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        sender.getPlayer().teleport(foundNPC.getLocation());
    }

    @CommandTabInformation(name = "teleport" , permission = "znpcs.cmd.teleport")
    public List<String> teleportTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList());
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"id", "height"}, name = "height", permission = "znpcs.cmd.height", help = {" &f&l* &e/znpcs height <npc_id> 2", "&8Set a greater or lesser distance of a hologram from the NPC."})
    public void changeHologramHeight(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        Integer id = Ints.tryParse(args.get("id"));
        if (id == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        NPC foundNPC = NPC.find(id);
        if (foundNPC == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
            return;
        }
        Double givenHeight = Doubles.tryParse(args.get("height"));
        if (givenHeight == null) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            return;
        }
        foundNPC.getNpcPojo().setHologramHeight(givenHeight);
        foundNPC.getHologram().createHologram();
        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
    }

    @CommandTabInformation(name = "height" , permission = "znpcs.cmd.height")
    public List<String> heightTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList());
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], NPC.all().stream().map(npc -> npc.getNpcPojo().getId()+"").collect(Collectors.toList()));
        }
        if (args.length == 2) {
            return Lists.newArrayList("<height>");
        }
        return Lists.newArrayList();
    }

    @CommandInformation(arguments = {"create", "remove", "gui", "set"}, name = "conversation", isMultiple = true, permission = "znpcs.cmd.conversation", help = {" &f&l* &e/znpcs conversation create first", " &f&l* &e/znpcs conversation remove first", " &f&l* &e/znpcs conversation set <npc_id> first [CLICK/RADIUS]", " &f&l* &e/znpcs conversation gui &8(&7Opens a GUI to manage conversations&8)", "&8RADIUS: &7Activates when the player is near the NPC", "&8CLICK: &7Activates when the player interacts with the NPC"})
    public void conversations(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            return;
        }
        if (args.containsKey("create")) {
            String conversationName = args.get("create");
            if (Conversation.exists(conversationName)) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.CONVERSATION_FOUND);
                return;
            }
            if (conversationName.length() < 3 || conversationName.length() > 16) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
                return;
            }
            ConfigurationConstants.NPC_CONVERSATIONS.add(new Conversation(conversationName));
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
        } else if (args.containsKey("remove")) {
            String conversationName = args.get("remove");
            if (!Conversation.exists(conversationName)) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_CONVERSATION_FOUND);
                return;
            }
            ConfigurationConstants.NPC_CONVERSATIONS.remove(Conversation.forName(conversationName));
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
        } else if (args.containsKey("gui")) {
            sender.getPlayer().openInventory((new ConversationGUI(sender.getPlayer())).build());
        } else if (args.containsKey("set")) {
            List<String> split = SPACE_SPLITTER.splitToList(args.get("set"));
            if (split.size() < 2) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.CONVERSATION_SET_INCORRECT_USAGE);
                return;
            }
            Integer id = Ints.tryParse(split.get(0));
            if (id == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                return;
            }
            NPC foundNPC = NPC.find(id);
            if (foundNPC == null) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                return;
            }
            String conversationName = split.get(1);
            if (Conversation.exists(conversationName)) {
                foundNPC.getNpcPojo().setConversation(new ConversationModel(conversationName, (split.size() > 1) ? split.get(2) : "CLICK"));
            } else {
                foundNPC.getNpcPojo().setConversation(null);
            }
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
        }
    }

    @CommandTabInformation(name = "conversation" , permission = "znpcs.cmd.conversation")
    public List<String> conversationTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Lists.newArrayList("create", "remove", "gui", "set");
        }
        if (args.length == 1) {
            return partialTabCompleteOptions(args[0], Lists.newArrayList("create", "remove", "gui", "set"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("set")) {
                Integer id = Ints.tryParse(args[1]);
                if (id == null) {
                    return List.of();
                }
                NPC npc = NPC.find(id);
                if (npc == null) {
                    return List.of();
                }

                return Lists.newArrayList(npc.getNpcPojo().getConversation() == null ? "null" : npc.getNpcPojo().getConversation().getConversationName());
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                return Lists.newArrayList("CLICK", "RADIUS");
            }
        }
        return Lists.newArrayList();
    }

    private List<String> partialTabCompleteOptions(String arg, List<String> completions) {
        if (arg.isEmpty()) {
            return completions;
        }
        List<String> result = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, completions, result);
        Collections.sort(result);
        return result;
    }

    interface SkinFunction {
        void apply(Player param1Player, NPC param1NPC, String param1String);
    }
}
