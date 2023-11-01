package lol.pyr.znpcsplus.interaction.consumer;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConsumerAction extends InteractionActionImpl {
    private final TaskScheduler scheduler;
    private final Consumer<Player> clickConsumer;
    private final ConsumerType consumerType;

    public ConsumerAction(TaskScheduler scheduler, Consumer<Player> clickConsumer, ConsumerType consumerType, InteractionType interactionType, long cooldown, long delay) {
        super(cooldown, delay, interactionType);
        this.scheduler = scheduler;
        this.clickConsumer = clickConsumer;
        this.consumerType = consumerType;
    }

    public ConsumerAction(TaskScheduler scheduler, Consumer<Player> clickConsumer, InteractionType interactionType, long cooldown, long delay) {
        this(scheduler, clickConsumer, ConsumerType.SYNC, interactionType, cooldown, delay);
    }

    @Override
    public boolean isApiOnly() {
        return true;
    }

    @Override
    public void run(Player player) {
        this.consumerType.getClickConsumer().accept(scheduler, () -> this.clickConsumer.accept(player));
    }

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("API Consumer Action (Immutable)", NamedTextColor.DARK_GREEN));
    }

    public Consumer<Player> getClickConsumer() {
        return clickConsumer;
    }

    public enum ConsumerType {
        SYNC(TaskScheduler::runSyncGlobal),
        ASYNC(TaskScheduler::runAsyncGlobal);
        private final BiConsumer<TaskScheduler, Runnable> clickConsumer;

        ConsumerType(BiConsumer<TaskScheduler, Runnable> clickConsumer) {
            this.clickConsumer = clickConsumer;
        }

        public BiConsumer<TaskScheduler, Runnable> getClickConsumer() {
            return clickConsumer;
        }
    }
}
