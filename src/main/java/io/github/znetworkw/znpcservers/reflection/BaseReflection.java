package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.Utils;
import lol.pyr.znpcsplus.ZNPCsPlus;

import java.util.List;

public abstract class BaseReflection <T> {
    protected final List<String> className;

    protected Class<?> BUILDER_CLASS;

    private T cached;

    private boolean loaded = false;

    protected BaseReflection(ReflectionBuilder builder) {
        this(builder.getClassName());
    }

    protected BaseReflection(List<String> className) {
        this.className = className;
        for (String classes : className) {
            try {
                this.BUILDER_CLASS = Class.forName(classes);
                break;
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    public T get() {
        return get(false);
    }

    public T get(boolean missAllowed) {
        if (this.loaded) return this.cached;
        try {
            if (this.BUILDER_CLASS == null) throw new ClassNotFoundException("No class found: " + className);
            T eval = (this.cached != null) ? this.cached : (this.cached = load());
            if (eval == null) throw new NullPointerException();
        } catch (Throwable throwable) {
            if (!missAllowed) {
                warn(getClass().getSimpleName() + " get failed!");
                warn("Class Names: " + className);
                warn("Loader Type: " + getClass().getCanonicalName());
                warn("Bukkit Version: " + Utils.BUKKIT_VERSION + " (" + Utils.getBukkitPackage() + ")");
                warn("Exception:");
                throwable.printStackTrace();
            }
        }
        this.loaded = true;
        return this.cached;
    }

    private void warn(String message) {
        ZNPCsPlus.LOGGER.warning("[Reflection] " + message);
    }

    protected abstract T load() throws Exception;
}