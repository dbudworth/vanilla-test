import java.util.Observable;
import java.util.Observer;

/**
 * Run with: -XX:-UseTLAB -XX:+DoEscapeAnalysis -XX:+AggressiveOpts
 * Created by: dbudworth at 4/18/13 9:05 PM
 */
public class TestIterations {
    static final int OBSERVER_COUNT = 10;
    public static void main(String... ignored) {
        runIterations(new ArraySynchronized(OBSERVER_COUNT));
        runIterations(new CasCalls(OBSERVER_COUNT));
        runIterations(new ArrayLocked(OBSERVER_COUNT));
    }

    public static void runIterations(Observers observers) {
        for (int x = 0; x < OBSERVER_COUNT; x++)
            observers.add(new MyObserver());


        long duration=0;
        long before = memoryUsed();
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            callObservers(observers);
            long end = System.nanoTime();
            duration = end - start;
        }
        long size = memoryUsed() - before;
        System.out.printf("10000x100 Iterations used %,d bytes, last 10k %,d ns%n", size, duration);
    }

    private static void callObservers(Observers observers) {
        for (int j = 0; j < 10000; j++) {
            observers.call(null, null);
        }
    }

    static int counter = 0;

    static class MyObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            counter++;
        }

    }

    public static long memoryUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

}