package lol.pyr.znpcsplus.npc;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.state.ModelState;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.model.IModel;
import com.ticxo.modelengine.api.nms.entity.impl.ManualRangeManager;
import com.ticxo.modelengine.api.nms.entity.wrapper.LookController;
import com.ticxo.modelengine.api.nms.entity.wrapper.RangeManager;
import com.ticxo.modelengine.api.nms.world.IDamageSource;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class ModeledNpcImpl extends Dummy implements Npc {

    private NpcLocation location;
    private World world;
    private ModelState modelState = ModelState.IDLE;

    private final Set<Player> viewers = new HashSet<>();

    private final List<InteractionAction> actions = new ArrayList<>();

    private final Map<EntityProperty<?>, Object> propertyMap = new HashMap<>();

    private LookController lookController;
    private ManualRangeManager rangeManager;

    public ModeledNpcImpl(NpcLocation location, World world) {
        this(UUID.randomUUID(), location, world);
    }

    public ModeledNpcImpl(UUID uuid, NpcLocation location, World world) {
        super(ModelEngineAPI.getEntityHandler().getEntityCounter().incrementAndGet(), uuid);
        if (location == null) throw new IllegalArgumentException("Location cannot be null");
        this.location = location;
        this.world = world;
    }

    @Override
    public LookController wrapLookControl() {
        if (lookController != null) return lookController;
        lookController = new NpcLookController(this);
        return lookController;
    }

    @Override
    public RangeManager wrapRangeManager(IModel model) {
        if(rangeManager != null )
            return rangeManager;
        rangeManager = new ManualRangeManager(this, model);
        return rangeManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        return hasProperty(key) ? (T) propertyMap.get((EntityPropertyImpl<?>) key) : key.getDefaultValue();
    }

    @Override
    public boolean hasProperty(EntityProperty<?> key) {
        return propertyMap.containsKey(key);
    }

    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        if (value == null || value.equals(key.getDefaultValue())) removeProperty(key);
        else propertyMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void UNSAFE_setProperty(EntityProperty<?> property, Object value) {
        setProperty((EntityProperty<T>) property, (T) value);
    }

    public void removeProperty(EntityProperty<?> key) {
        propertyMap.remove(key);
    }

    @Override
    public Hologram getHologram() {
        return null;
    }

    @Override
    public void delete() {
        if (ModelEngineAPI.getModeledEntity(getUniqueId()) != null) {
            ModelEngineAPI.getModeledEntity(getUniqueId()).destroy();
        }
    }

    @Override
    public void respawn() {
        delete();
        ModelEngineAPI.createModeledEntity(this);
    }

    @Override
    public NpcLocation getNpcLocation() {
        return location;
    }

    @Override
    public void setNpcLocation(NpcLocation npcLocation) {
        this.location = npcLocation;
        viewers.forEach(this::updateModel);
    }

    public void updateModel(Player player) {
        ModelEngineAPI.getModeledEntity(getUniqueId()).getRangeManager().updatePlayer(player);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public NpcType getType() {
        return null;
    }

    @Override
    public List<InteractionAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public void removeAction(int index) {
        actions.remove(index);
    }

    @Override
    public void editAction(int index, InteractionAction newAction) {
        actions.set(index, newAction);
    }

    @Override
    public void addAction(InteractionAction parse) {
        actions.add(parse);
    }

    @Override
    public boolean isShown(Player player) {
        return viewers.contains(player);
    }

    @Override
    public void hide(Player player) {
        ModelEngineAPI.getModeledEntity(getUniqueId()).hideFromPlayer(player);
        viewers.remove(player);
    }

    @Override
    public void show(Player player) {
        ModelEngineAPI.getModeledEntity(getUniqueId()).showToPlayer(player);
        viewers.add(player);
    }

    @Override
    public Location getLocation() {
        if (location == null) return null;
        return location.toBukkitLocation(world);
    }

    public ModelState getModelState() {
        return modelState;
    }

    @Override
    public boolean onHurt(IDamageSource source, float amount) {
        Bukkit.broadcastMessage("damaged custom model");
        return false;
    }

    @Override
    public void onInteract(Player player, EquipmentSlot hand) {
        Bukkit.broadcastMessage("interacted with custom model");
    }
}
