package lol.pyr.znpcsplus.scheduling;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepeatingTaskGuardTest {
    @Test
    void logsFirstFailureAndSuppressesBurst() {
        CapturingHandler handler = new CapturingHandler();
        Logger logger = newLogger(handler);

        Runnable guarded = RepeatingTaskGuard.wrap(logger, "npc-processor", () -> {
            throw new IllegalStateException("boom");
        }, 60_000L);

        guarded.run();
        guarded.run();
        guarded.run();

        assertEquals(1, handler.count(Level.SEVERE));
        assertEquals(0, handler.count(Level.WARNING));
    }

    @Test
    void reportsSuppressedExceptionsOnRecovery() {
        CapturingHandler handler = new CapturingHandler();
        Logger logger = newLogger(handler);

        AtomicInteger calls = new AtomicInteger();
        Runnable guarded = RepeatingTaskGuard.wrap(logger, "npc-processor", () -> {
            if (calls.getAndIncrement() < 2) throw new IllegalStateException("boom");
        }, 60_000L);

        guarded.run(); // severe
        guarded.run(); // suppressed
        guarded.run(); // recovery warning

        assertEquals(1, handler.count(Level.SEVERE));
        assertEquals(1, handler.count(Level.WARNING));
        assertTrue(handler.firstMessage(Level.WARNING).contains("recovered"));
    }

    @Test
    void logsAgainAfterIntervalAndReportsSuppressedCount() throws InterruptedException {
        CapturingHandler handler = new CapturingHandler();
        Logger logger = newLogger(handler);

        Runnable guarded = RepeatingTaskGuard.wrap(logger, "npc-processor", () -> {
            throw new IllegalStateException("boom");
        }, 5L);

        guarded.run(); // severe
        guarded.run(); // suppressed
        Thread.sleep(15L);
        guarded.run(); // warning + severe

        assertEquals(2, handler.count(Level.SEVERE));
        assertEquals(1, handler.count(Level.WARNING));
        assertTrue(handler.firstMessage(Level.WARNING).contains("Suppressed"));
    }

    private static Logger newLogger(CapturingHandler handler) {
        Logger logger = Logger.getLogger("znpcsplus-test-" + UUID.randomUUID());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(handler);
        return logger;
    }

    private static final class CapturingHandler extends Handler {
        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        private int count(Level level) {
            int count = 0;
            for (LogRecord record : records) {
                if (record.getLevel().equals(level)) count++;
            }
            return count;
        }

        private String firstMessage(Level level) {
            for (LogRecord record : records) {
                if (record.getLevel().equals(level)) return record.getMessage();
            }
            return "";
        }
    }
}
