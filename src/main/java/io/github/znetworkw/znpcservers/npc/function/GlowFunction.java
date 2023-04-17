package io.github.znetworkw.znpcservers.npc.function;

import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.FunctionContext;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCFunction;

import java.lang.reflect.Constructor;

public class GlowFunction extends NPCFunction {
    public GlowFunction() {
        super("glow");
    }

    protected NPCFunction.ResultType runFunction(NPC npc, FunctionContext functionContext) {
        if (!(functionContext instanceof FunctionContext.ContextWithValue))
            throw new IllegalStateException("invalid context type, " + functionContext.getClass().getSimpleName() + ", expected ContextWithValue.");
        String glowColorName = ((FunctionContext.ContextWithValue) functionContext).getValue();
        try {
            Object glowColor = CacheRegistry.ENUM_CHAT_FORMAT_FIND.load().invoke(null, (
                    glowColorName == null || glowColorName.length() == 0) ? "WHITE" : glowColorName);
            if (glowColor == null)
                return NPCFunction.ResultType.FAIL;
            npc.getNpcPojo().setGlowName(glowColorName);
            npc.setGlowColor(glowColor);
            CacheRegistry.SET_DATA_WATCHER_METHOD.load().invoke(CacheRegistry.GET_DATA_WATCHER_METHOD
                    .load().invoke(npc.getNmsEntity()), ((Constructor) CacheRegistry.DATA_WATCHER_OBJECT_CONSTRUCTOR
                    .load()).newInstance(Integer.valueOf(0), CacheRegistry.DATA_WATCHER_REGISTER_FIELD
                    .load()), Byte.valueOf(!FunctionFactory.isTrue(npc, this) ? 64 : 0));
            npc.getPackets().getProxyInstance().update(npc.getPackets());
            npc.deleteViewers();
            return NPCFunction.ResultType.SUCCESS;
        } catch (ReflectiveOperationException operationException) {
            return NPCFunction.ResultType.FAIL;
        }
    }

    protected boolean allow(NPC npc) {
        return npc.getPackets().getProxyInstance().allowGlowColor();
    }

    public NPCFunction.ResultType resolve(NPC npc) {
        return runFunction(npc, new FunctionContext.ContextWithValue(npc, npc.getNpcPojo().getGlowName()));
    }
}
