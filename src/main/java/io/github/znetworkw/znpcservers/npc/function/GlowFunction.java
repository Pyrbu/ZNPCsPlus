package io.github.znetworkw.znpcservers.npc.function;

import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.npc.FunctionContext;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCFunction;

public class GlowFunction extends NPCFunction {
    public GlowFunction() {
        super("glow");
    }

    protected NPCFunction.ResultType runFunction(NPC npc, FunctionContext functionContext) {
        if (!(functionContext instanceof FunctionContext.ContextWithValue))
            throw new IllegalStateException("invalid context type, " + functionContext.getClass().getSimpleName() + ", expected ContextWithValue.");
        String glowColorName = ((FunctionContext.ContextWithValue) functionContext).getValue();
        try {
            Object glowColor = Reflections.ENUM_CHAT_FORMAT_FIND.get().invoke(null, (
                    glowColorName == null || glowColorName.length() == 0) ? "WHITE" : glowColorName);
            if (glowColor == null)
                return NPCFunction.ResultType.FAIL;
            npc.getNpcPojo().setGlowName(glowColorName);
            npc.setGlowColor(glowColor);
            Reflections.SET_DATA_WATCHER_METHOD.get().invoke(Reflections.GET_DATA_WATCHER_METHOD
                    .get().invoke(npc.getNmsEntity()), Reflections.DATA_WATCHER_OBJECT_CONSTRUCTOR
                    .get().newInstance(0, Reflections.DATA_WATCHER_REGISTER_FIELD
                    .get()), (byte) (!FunctionFactory.isTrue(npc, this) ? 64 : 0));
            npc.getPackets().getNms().update(npc.getPackets());
            npc.deleteViewers();
            return NPCFunction.ResultType.SUCCESS;
        } catch (ReflectiveOperationException operationException) {
            return NPCFunction.ResultType.FAIL;
        }
    }

    protected boolean allow(NPC npc) {
        return npc.getPackets().getNms().allowGlowColor();
    }

    public NPCFunction.ResultType resolve(NPC npc) {
        return runFunction(npc, new FunctionContext.ContextWithValue(npc, npc.getNpcPojo().getGlowName()));
    }
}
