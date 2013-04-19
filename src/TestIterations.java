import java.util.Observable;
import java.util.Observer;

/**
 * Run with: -XX:-UseTLAB -XX:+DoEscapeAnalysis -XX:+AggressiveOpts
 * Created by: dbudworth at 4/18/13 9:05 PM
 */
public class TestIterations {
    static final int OBSERVER_COUNT = 10;

    public static void main(String... ignored) {
        for (int i = 0; i < 3; i++) {
            runIterations(new ArraySynchronized(OBSERVER_COUNT));
            runIterations(new CasCalls(OBSERVER_COUNT));
            runIterations(new ArrayLocked(OBSERVER_COUNT));
            runIterations(new VanillaObservers(OBSERVER_COUNT));
        }
    }

    public static void runIterations(Observers observers) {
        for (int x = 0; x < OBSERVER_COUNT; x++)
            observers.add(new MyObserver());


        long before = memoryUsed();
        int repeats = 10000000;
        long start = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
            callObservers(observers);
        }
        long duration = System.nanoTime() - start;
        long size = memoryUsed() - before;
        System.out.printf("%d Iterations used %,d bytes, took %,d ns per loop%n\t%s%n",
                repeats, size, duration / repeats, observers);
    }

    private static void callObservers(Observers observers) {
        observers.call(null, null);
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