package io.github.znetworkw.znpcservers.npc.nms;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class PacketCache {
    protected static final ImmutableMap<Method, PacketValue> VALUE_LOOKUP_BY_NAME;

    static {
        ImmutableMap.Builder<Method, PacketValue> methodPacketValueBuilder = ImmutableMap.builder();
        for (Method method : NMS.class.getMethods()) {
            if (method.isAnnotationPresent(PacketValue.class))
                methodPacketValueBuilder.put(method, method.getAnnotation(PacketValue.class));
        }
        VALUE_LOOKUP_BY_NAME = methodPacketValueBuilder.build();
    }

    private final Map<String, Object> packetResultCache = new ConcurrentHashMap<>();

    private final NMS nms;

    public PacketCache(NMS NMS) {
        this.nms = newNMSInstance(NMS);
    }

    public PacketCache() {
        this(NMSFactory.NMS_FOR_CURRENT_VERSION);
    }

    public NMS getNms() {
        return this.nms;
    }

    protected NMS newNMSInstance(NMS NMS) {
        return (NMS) Proxy.newProxyInstance(NMS
                .getClass().getClassLoader(), new Class[]{NMS.class}, new PacketHandler(this, NMS));
    }

    private Object getOrCache(NMS instance, Method method, Object[] args) {
        if (!VALUE_LOOKUP_BY_NAME.containsKey(method))
            throw new IllegalStateException("value not found for method: " + method.getName());
        PacketValue packetValue = VALUE_LOOKUP_BY_NAME.get(method);
        assert packetValue != null;
        String keyString = packetValue.valueType().resolve(packetValue.keyName(), args);
        return this.packetResultCache.computeIfAbsent(keyString, o -> {
            try {
                return method.invoke(instance, args);
            } catch (InvocationTargetException | IllegalAccessException operationException) {
                throw new AssertionError("can't invoke method: " + method.getName(), operationException);
            }
        });
    }

    public void flushCache(String... strings) {
        Set<Map.Entry<String, Object>> set = this.packetResultCache.entrySet();
        for (String string : strings)
            set.removeIf(entry -> entry.getKey().startsWith(string));
    }

    public void flushCache() {
        flushCache(VALUE_LOOKUP_BY_NAME.values().stream()
                .map(PacketValue::keyName).toArray(String[]::new));
    }

    private static class PacketHandler implements InvocationHandler {
        private final PacketCache packetCache;

        private final NMS packets;

        public PacketHandler(PacketCache packetCache, NMS packets) {
            this.packetCache = packetCache;
            this.packets = packets;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (PacketCache.VALUE_LOOKUP_BY_NAME.containsKey(method))
                return this.packetCache.getOrCache(this.packets, method, args);
            return method.invoke(this.packets, args);
        }
    }
}
