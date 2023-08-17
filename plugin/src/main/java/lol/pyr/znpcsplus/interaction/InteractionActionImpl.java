package lol.pyr.znpcsplus.interaction;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import net.kyori.adventure.text.Component;

public abstract class InteractionActionImpl extends InteractionAction {
    protected InteractionActionImpl(long cooldown, long delay, InteractionType interactionType) {
        super(cooldown, delay, interactionType);
    }

    public abstract Component getInfo(String id, int index, CommandContext context);
}
