import java.util.*;

/**
 * @author peter.lawrey
 */
public class VanillaObservers implements Observers {
    private final Set<Observer> observerSet = new LinkedHashSet<Observer>();
    private final int[] first, total;
    private int idx;
    private volatile Observer[] observers = {};

    public VanillaObservers(int initialSize) {
        first = new int[initialSize];
        total = new int[initialSize];
    }


    @Override
    public void add(Observer observer) {
        synchronized (observerSet) {
            if (observerSet.add(observer))
                observers = observerSet.toArray(new Observer[observerSet.size()]);
        }
    }

    @Override
    public void remove(Observer observer) {
        synchronized (observerSet) {
            if (observerSet.remove(observer))
                observers = observerSet.toArray(new Observer[observerSet.size()]);
        }
    }

    @Override
    public void call(Observable observable, Object arg) {
        final Observer[] observers = this.observers;

        idx++;
        if (idx >= observers.length)
            idx = 0;

        ++first[idx];
        for (int i = idx; i < observers.length; i++) {
            observers[i].update(observable, arg);
            ++total[i];
        }
        for (int i = 0; i < idx; i++) {
            observers[i].update(observable, arg);
            ++total[i];
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "sz=" + observers.length +
                ", idx=" + idx +
                ", first=" + Arrays.toString(first) +
                ", total=" + Arrays.toString(total) +
                '}';
    }
}
