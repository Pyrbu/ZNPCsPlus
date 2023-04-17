package io.github.znetworkw.znpcservers.commands.list.inventory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.NPCAction;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationKey;
import io.github.znetworkw.znpcservers.user.EventService;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventory;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryPage;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ConversationGUI extends ZInventory {
    private static final String WHITESPACE = " ";

    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");

    private static final Joiner SPACE_JOINER = Joiner.on(" ");

    public ConversationGUI(Player player) {
        super(player);
        setCurrentPage(new MainPage(this));
    }

    static class MainPage extends ZInventoryPage {
        public MainPage(ZInventory inventory) {
            super(inventory, "Conversations", 5);
        }

        public void update() {
            for (int i = 0; i < ConfigurationConstants.NPC_CONVERSATIONS.size(); i++) {
                Conversation conversation = ConfigurationConstants.NPC_CONVERSATIONS.get(i);
                addItem(ItemStackBuilder.forMaterial(Material.PAPER).setName(ChatColor.GREEN + conversation.getName()).setLore(new String[]{"&7this conversation has &b" + conversation.getTexts().size() + " &7texts,", "&7it will activate when a player is on a &b" + conversation.getRadius() + "x" + conversation.getRadius() + " &7radius,", "&7or when a player interacts with an npc.", "&7when the conversation is finish, there is a &b" + conversation.getDelay() + "s &7delay to start again.", "&f&lUSES", " &bLeft-click &7to manage texts.", " &bRight-click &7to add a new text.", " &bQ &7to change the radius.", " &bMiddle-click &7to change the cooldown."}, ).build(), i, clickEvent -> {
                    if (clickEvent.getClick() == ClickType.DROP) {
                        Utils.sendTitle(getPlayer(), "&b&lCHANGE RADIUS", "&7Type the new radius...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                    } else if (clickEvent.isRightClick()) {
                        Utils.sendTitle(getPlayer(), "&e&lADD LINE", "&7Type the new line...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                    } else if (clickEvent.isLeftClick()) {
                        (new EditConversationPage(getInventory(), conversation)).openInventory();
                    } else if (clickEvent.getClick() == ClickType.MIDDLE) {
                        Utils.sendTitle(getPlayer(), "&6&lCHANGE COOLDOWN", "&7Type the new cooldown...");
                        EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                    }
                });
            }
        }

        static class EditConversationPage extends ZInventoryPage {
            private final Conversation conversation;

            public EditConversationPage(ZInventory inventory, Conversation conversation) {
                super(inventory, "Editing conversation " + conversation.getName(), 5);
                this.conversation = conversation;
            }

            public void update() {
                for (int i = 0; i < this.conversation.getTexts().size(); i++) {
                    ConversationKey conversationKey = this.conversation.getTexts().get(i);
                    addItem(ItemStackBuilder.forMaterial(Material.NAME_TAG).setName(ChatColor.AQUA + conversationKey.getTextFormatted() + "....").setLore(new String[]{
                            "&7this conversation text has a delay of &b" + conversationKey.getDelay() + "s &7to be executed,", "&7the sound for the text is &b" + ((conversationKey.getSoundName() == null) ? "NONE" : conversationKey.getSoundName()) + "&7,", "&7before sending the text there is a delay of &b" + conversationKey.getDelay() + "s", "&7the index for the text is &b" + i + "&7,", "&7and the conversation has currently &b" + conversationKey.getActions().size() + " actions&7.", "&f&lUSES", " &bLeft-click &7to change the position.", " &bRight-click &7to remove text.", " &bLeft-Shift-click &7to change the sound.", " &bMiddle-click &7to change the delay.",
                            " &bRight-Shift-click &7to edit the text.", " &bQ &7to manage actions."}, ).build(), i, clickEvent -> {
                        if (clickEvent.getClick() == ClickType.SHIFT_LEFT) {
                            Utils.sendTitle(getPlayer(), "&c&lCHANGE SOUND", "&7Type the new sound...");
                            EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                        } else if (clickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                            Utils.sendTitle(getPlayer(), "&a&lEDIT TEXT", "&7Type the new text...");
                            EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                        } else if (clickEvent.isLeftClick()) {
                            Utils.sendTitle(getPlayer(), "&e&lCHANGE POSITION &a>=0&c<=" + this.conversation.getTexts().size(), "&7Type the new position...");
                            EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                        } else if (clickEvent.isRightClick()) {
                            this.conversation.getTexts().remove(conversationKey);
                            Configuration.MESSAGES.sendMessage(getPlayer(), ConfigurationValue.SUCCESS);
                            openInventory();
                        } else if (clickEvent.getClick() == ClickType.MIDDLE) {
                            Utils.sendTitle(getPlayer(), "&d&lCHANGE DELAY", "&7Type the new delay...");
                            EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                        } else if (clickEvent.getClick() == ClickType.DROP) {
                            (new ConversationGUI.MainPage.ActionManagementPage(getInventory(), this.conversation, conversationKey)).openInventory();
                        }
                    });
                }
            }
        }

        static class ActionManagementPage extends ZInventoryPage {
            private final Conversation conversation;

            private final ConversationKey conversationKey;

            public ActionManagementPage(ZInventory inventory, Conversation conversation, ConversationKey conversationKey) {
                super(inventory, "Editing " + conversationKey.getTextFormatted(), 5);
                this.conversation = conversation;
                this.conversationKey = conversationKey;
            }

            public void update() {
                for (int i = 0; i < this.conversationKey.getActions().size(); i++) {
                    NPCAction znpcAction = this.conversationKey.getActions().get(i);
                    addItem(ItemStackBuilder.forMaterial(Material.ANVIL).setName(ChatColor.AQUA + znpcAction.getAction().substring(0, Math.min(znpcAction.getAction().length(), 24)) + "....").setLore("&7this action type is &b" + znpcAction.getActionType(), "&f&lUSES", " &bRight-click &7to remove text.").build(), i, clickEvent -> {
                        if (clickEvent.isRightClick()) {
                            this.conversationKey.getActions().remove(znpcAction);
                            Configuration.MESSAGES.sendMessage(getPlayer(), ConfigurationValue.SUCCESS);
                            openInventory();
                        }
                    });
                }
                addItem(ItemStackBuilder.forMaterial(Material.EMERALD).setName(ChatColor.AQUA + "ADD A NEW ACTION").setLore(new String[]{"&7click here..."}, ).build(), getRows() - 5, clickEvent -> {
                    Utils.sendTitle(getPlayer(), "&d&lADD ACTION", "&7Type the action...");
                    EventService.addService(ZUser.find(getPlayer()), AsyncPlayerChatEvent.class).addConsumer(()).addConsumer(());
                });
            }
        }
    }
}
