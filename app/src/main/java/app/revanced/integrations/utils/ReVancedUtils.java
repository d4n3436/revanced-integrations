package app.revanced.integrations.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.Bidi;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import app.revanced.integrations.utils.LogHelper;

public class ReVancedUtils {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static Context getContext() {
        return context;
    }

    private ReVancedUtils() {
    } // utility class

    /**
     * Maximum number of background threads run concurrently
     */
    private static final int SHARED_THREAD_POOL_MAXIMUM_BACKGROUND_THREADS = 20;

    /**
     * General purpose pool for network calls and other background tasks.
     * All tasks run at max thread priority.
     */

    private static final ThreadPoolExecutor backgroundThreadPool = new ThreadPoolExecutor(
            2, // minimum 2 threads always ready to be used
            10, // For any threads over the minimum, keep them alive 10 seconds after they go idle
            SHARED_THREAD_POOL_MAXIMUM_BACKGROUND_THREADS,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setPriority(Thread.MAX_PRIORITY); // run at max priority
                    return t;
                }
            });

    public static void runOnBackgroundThread(Runnable task) {
        backgroundThreadPool.execute(task);
    }

    public static <T> Future<T> submitOnBackgroundThread(Callable<T> call) {
        Future<T> future = backgroundThreadPool.submit(call);
        return future;
    }

    public static boolean containsAny(final String value, final String... targets) {
        for (String string : targets)
            if (!string.isEmpty() && value.contains(string)) return true;
        return false;
    }

    private static Boolean isRightToLeftTextLayout;
    /**
     * If the device language uses right to left text layout (hebrew, arabic, etc)
     */
    public static boolean isRightToLeftTextLayout() {
        if (isRightToLeftTextLayout == null) {
            String displayLanguage = Locale.getDefault().getDisplayLanguage();
            isRightToLeftTextLayout = new Bidi(displayLanguage, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isRightToLeft();
        }
        return isRightToLeftTextLayout;
    }

    public static void showToastShort(Context context, String messageToToast) {
        showToast(context, messageToToast, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String messageToToast) {
        showToast(context, messageToToast, Toast.LENGTH_LONG);
    }

    /**
     * Safe to call from any thread
     */
    public static void showToastShort(@NonNull String messageToToast) {
        showToast(context, messageToToast, Toast.LENGTH_SHORT);
    }

    /**
     * Safe to call from any thread
     */
    public static void showToastLong(@NonNull String messageToToast) {
        showToast(context, messageToToast, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, @NonNull String messageToToast, int toastDuration) {
        Objects.requireNonNull(messageToToast);
        runOnMainThreadNowOrLater(() -> {
                    // cannot use getContext(), otherwise if context is null it will cause infinite recursion of error logging
                    assert context != null;
                    Toast.makeText(context, messageToToast, toastDuration).show();
                }
        );
    }

    /**
     * Automatically logs any exceptions the runnable throws
     */
    public static void runOnMainThread(Runnable runnable) {
        runOnMainThreadDelayed(runnable, 0);
    }

    /**
     * Automatically logs any exceptions the runnable throws
     */
    public static void runOnMainThreadDelayed(Runnable runnable, long delayMillis) {
        Runnable loggingRunnable = () -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                LogHelper.printException(ReVancedUtils.class, runnable.getClass() + ": " + ex.getMessage(), ex);
            }
        };
        new Handler(Looper.getMainLooper()).postDelayed(loggingRunnable, delayMillis);
    }

    /**
     * If called from the main thread, the code is run immediately.<p>
     * If called off the main thread, this is the same as {@link #runOnMainThread(Runnable)}.
     */
    public static void runOnMainThreadNowOrLater(@NonNull Runnable runnable) {
        if (currentlyIsOnMainThread()) {
            runnable.run();
        } else {
            runOnMainThread(runnable);
        }
    }

    /**
     * @return if the calling thread is on the main thread
     */
    public static boolean currentlyIsOnMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * @throws IllegalStateException if the calling thread is _off_ the main thread
     */
    public static void verifyOnMainThread() throws IllegalStateException {
        if (!currentlyIsOnMainThread()) {
            throw new IllegalStateException("Must call _on_ the main thread");
        }
    }

    /**
     * @throws IllegalStateException if the calling thread is _on_ the main thread
     */
    public static void verifyOffMainThread() throws IllegalStateException {
        if (currentlyIsOnMainThread()) {
            throw new IllegalStateException("Must call _off_ the main thread");
        }
    }
}