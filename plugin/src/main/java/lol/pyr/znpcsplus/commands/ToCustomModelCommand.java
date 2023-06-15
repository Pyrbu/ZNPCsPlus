package lol.pyr.znpcsplus.commands;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.ModeledNpcImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class ToCustomModelCommand implements CommandHandler {

    private final NpcRegistryImpl npcRegistry;
    private final BukkitAudiences adventure;

    public ToCustomModelCommand(NpcRegistryImpl npcRegistry, BukkitAudiences adventure) {
        this.npcRegistry = npcRegistry;
        this.adventure = adventure;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " tocustommodel <id> <model>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        String modelname = context.popString();
        ActiveModel model = ModelEngineAPI.createActiveModel(modelname);
        if (model == null) {
            adventure.sender(context.getSender()).sendMessage(Component.text("Model not found: " + modelname, NamedTextColor.RED));
            return;
        }
        ModeledNpcImpl entity = new ModeledNpcImpl(entry.getNpc().getNpcLocation(), entry.getNpc().getWorld());
        entity.setHitbox(model.getBlueprint().getMainHitbox());
        NpcEntryImpl modeledEntry = new NpcEntryImpl(entry.getId() + "_model", entity);
        npcRegistry.add(modeledEntry);
        modeledEntry.enableEverything();
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
        if (modeledEntity == null) {
            adventure.sender(context.getSender()).sendMessage(Component.text("Failed to create modeled entity", NamedTextColor.RED));
            return;
        }
        entity.wrapRangeManager(modeledEntity);
        modeledEntity.addModel(model, false);
        modeledEntity.setBaseEntityVisible(false);
        modeledEntity.getRangeManager().setRenderDistance(100);
//        modeledEntity.setModelRotationLock(true);
        modeledEntity.setState(entity.getModelState());
        if (!modeledEntity.isInitialized()) {
            adventure.sender(context.getSender()).sendMessage(Component.text("Failed to initialize modeled entity", NamedTextColor.RED));
        }
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestCollection(ModelEngineAPI.api.getModelRegistry().getAllBlueprintId());
        return Collections.emptyList();
    }
}
