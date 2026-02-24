package lol.pyr.znpcsplus.scheduling;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RepeatingTaskGuard {
    private final Logger logger;
    private final String taskName;
    private final long minErrorLogIntervalMs;

    private long lastErrorLogAt;
    private int suppressedErrors;

    private RepeatingTaskGuard(Logger logger, String taskName, long minErrorLogIntervalMs) {
        this.logger = Objects.requireNonNull(logger, "logger");
        this.taskName = Objects.requireNonNull(taskName, "taskName");
        if (minErrorLogIntervalMs < 0) throw new IllegalArgumentException("minErrorLogIntervalMs must be >= 0");
        this.minErrorLogIntervalMs = minErrorLogIntervalMs;
    }

    public static Runnable wrap(Logger logger, String taskName, Runnable runnable) {
        return wrap(logger, taskName, runnable, 30_000L);
    }

    public static Runnable wrap(Logger logger, String taskName, Runnable runnable, long minErrorLogIntervalMs) {
        Objects.requireNonNull(runnable, "runnable");
        RepeatingTaskGuard guard = new RepeatingTaskGuard(logger, taskName, minErrorLogIntervalMs);
        return () -> guard.runSafely(runnable);
    }

    private synchronized void runSafely(Runnable runnable) {
        try {
            runnable.run();
            if (suppressedErrors > 0) {
                logger.warning("Task '" + taskName + "' recovered after suppressing " + suppressedErrors + " exception(s).");
                suppressedErrors = 0;
            }
        } catch (Throwable throwable) {
            long now = System.currentTimeMillis();
            if (shouldLog(now)) {
                if (suppressedErrors > 0) {
                    logger.warning("Suppressed " + suppressedErrors + " repeated exception(s) in task '" + taskName + "'.");
                    suppressedErrors = 0;
                }
                logger.log(Level.SEVERE, "Repeating task '" + taskName + "' failed.", throwable);
                lastErrorLogAt = now;
            } else {
                suppressedErrors++;
            }
        }
    }

    private boolean shouldLog(long now) {
        return lastErrorLogAt == 0L || (now - lastErrorLogAt) >= minErrorLogIntervalMs;
    }
}
