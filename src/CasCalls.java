import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: dbudworth
 * Date: 4/18/13
 * Time: 10:00 PM
 */
class CasCalls implements Observers {
    private AtomicReference[] observers;
    int sz = 0;
    int idx = 0;
    private int first[] = new int[0];
    private int total[] = new int[0];

    public CasCalls(Observer... observers) {
        this(observers.length);
        for (Observer o : observers) this.observers[sz++].set(o);
    }

    public CasCalls(int initialSize) {
        observers = new AtomicReference[initialSize];
        for (int x = 0; x < initialSize;x++) observers[x] = new AtomicReference();
        first = new int[initialSize];
        total = new int[initialSize];
    }

    @Override
    public void call(Observable observable, Object arg) {
        idx++;
        for (int x = 0; x < observers.length; x++) {
            int use = (idx + x) % observers.length;
            if (x == 0)
                ++first[use];
            ++total[use];
            Observer o = (Observer) observers[use].get();
            if (o != null)
                o.update(observable, arg);
        }
    }

    @Override
    public void add(Observer observer) {
        synchronized (observers) {
            //fast skip of a full array, assumed to be common case
            if (sz < observers.length) {
                for (int x = 0; x < observers.length; x++) {
                    if (observers[x].compareAndSet(null,observer)){
                        first[x]=0;
                        ++sz;
                        return;
                    }
                }
            }
            int newsz = observers.length + 1;
            AtomicReference[] newObservers = Arrays.copyOf(observers, newsz);
            first = Arrays.copyOf(first, newsz); //won't actually work threaded
            total = Arrays.copyOf(first, newsz); //won't actually work threaded
            newObservers[observers.length - 1] = new AtomicReference(observer);
            observers = newObservers; //need memory fence here?
        }
    }

    @Override
    public void remove(Observer observer) {
        synchronized (observers) {
            for (int x = 0; x < observers.length; x++) {
                if (observers[x].compareAndSet(observer,null)) {
                    --sz; //don't shrink array
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" +
                "sz=" + sz +
                ", idx=" + idx +
                ", first=" + Arrays.toString(first) +
                ", total=" + Arrays.toString(total) +
                '}';
    }

}
