package lol.pyr.znpcsplus.reflection;

import com.github.retrooper.packetevents.PacketEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class ReflectionLazyLoader<T> {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Reflection");
    protected final List<String> possibleClassNames;
    protected List<Class<?>> reflectionClasses = new ArrayList<>();
    protected final boolean strict;
    private T cached;
    private boolean loaded = false;

    protected ReflectionLazyLoader(ReflectionBuilder builder) {
        this(builder.getClassNames(), builder.isStrict());
    }

    protected ReflectionLazyLoader(List<String> possibleClassNames, boolean strict) {
        this.possibleClassNames = possibleClassNames;
        this.strict = strict;
        for (String name : possibleClassNames) try {
            reflectionClasses.add(Class.forName(name));
        } catch (ClassNotFoundException ignored) {}
    }

    public T get() {
        if (this.loaded) return this.cached;
        try {
            if (this.reflectionClasses.size() == 0) throw new ClassNotFoundException("No class found: " + possibleClassNames);
            T eval = (this.cached != null) ? this.cached : (this.cached = load());
            if (eval == null) throw new RuntimeException("Returned value is null");
        } catch (Throwable throwable) {
            if (strict) {
                logger.warning(" ----- REFLECTION FAILURE DEBUG INFORMATION, REPORT THIS ON THE ZNPCSPLUS GITHUB ----- ");
                logger.warning(getClass().getSimpleName() + " failed!");
                logger.warning("Class Names: " + possibleClassNames);
                logger.warning("Reflection Type: " + getClass().getCanonicalName());
                logger.warning("Server Version: " + PacketEvents.getAPI().getServerManager().getVersion().name());
                printDebugInfo(logger::warning);
                logger.warning("Exception:");
                throwable.printStackTrace();
                logger.warning(" ----- REFLECTION FAILURE DEBUG INFORMATION, REPORT THIS ON THE ZNPCSPLUS GITHUB ----- ");
            }
        }
        this.loaded = true;
        return this.cached;
    }

    protected abstract T load() throws Exception;
    protected void printDebugInfo(Consumer<String> logger) {}
}
