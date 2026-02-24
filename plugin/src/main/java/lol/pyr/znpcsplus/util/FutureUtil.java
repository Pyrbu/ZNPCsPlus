package lol.pyr.znpcsplus.util;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FutureUtil {
  public static CompletableFuture<Void> allOf(Collection<CompletableFuture<?>> futures) {
    return exceptionPrintingRunAsync(
        () -> {
          for (CompletableFuture<?> future : futures) future.join();
        });
  }

  public static <T> CompletableFuture<T> newExceptionPrintingFuture() {
    return new CompletableFuture<T>()
        .exceptionally(
            throwable -> {
              throwable.printStackTrace();
              return null;
            });
  }

  public static CompletableFuture<Void> exceptionPrintingRunAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable)
        .exceptionally(
            throwable -> {
              throwable.printStackTrace();
              return null;
            });
  }

  public static <T> CompletableFuture<T> exceptionPrintingSupplyAsync(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier)
        .exceptionally(
            throwable -> {
              throwable.printStackTrace();
              return null;
            });
  }
}
