package lol.pyr.znpcsplus.util;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class FutureUtil {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static CompletableFuture<Void> allOf(Collection<CompletableFuture<?>> futures) {
        return exceptionPrintingRunAsync(() -> {
            for (CompletableFuture<?> future : futures) future.join();
        });
    }

    public static <T> CompletableFuture<T> newExceptionPrintingFuture() {
        return new CompletableFuture<T>().exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public static CompletableFuture<Void> exceptionPrintingRunAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public static <T> CompletableFuture<T> exceptionPrintingSupplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public static void shutdownExecutor() {
        executor.shutdownNow();
    }
}
