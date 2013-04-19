import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
* Created with IntelliJ IDEA.
* User: dbudworth
* Date: 4/18/13
* Time: 10:00 PM
* To change this template use File | Settings | File Templates.
*/
class ArraySynchronized implements Observers{
    private Observer[] observers;
    int sz = 0;
    int idx = 0;
    private int first[] = new int[0];
    private int total[] = new int[0];

    public ArraySynchronized(Observer... observers){
        this(observers.length);
        for (Observer o : observers) this.observers[sz++] = o;
    }
    public ArraySynchronized(int initialSize){
        observers = new Observer[initialSize];
        first = new int[initialSize];
        total = new int[initialSize];
    }

    @Override
    public void call(Observable observable, Object arg) {
        synchronized (observers) {
            idx++;
            for (int x = 0; x < observers.length; x++) {
                int use = (idx + x) % observers.length;
                if (x == 0)
                    ++first[use];
                ++total[use];
                Observer o = observers[use];
                if (o != null)
                    o.update(observable, arg);
            }
        }
    }

    @Override
    public void add(Observer observer) {
        synchronized (observers) {
            //fast skip of a full array, assumed to be common case
            if (sz < observers.length) {
                for (int x = 0; x < observers.length; x++) {
                    if (observers[x] == null) {
                        observers[x] = observer;
                        first[x] = 0;
                        ++sz;
                        return;
                    }
                }
            }
            int newsz = observers.length+1;
            observers = Arrays.copyOf(observers, newsz);
            first = Arrays.copyOf(first, newsz);
            total = Arrays.copyOf(first, newsz);
            observers[observers.length - 1] = observer;
        }
    }

    @Override
    public void remove(Observer observer) {
        synchronized (observers) {
            for (int x = 0; x < observers.length; x++) {
                if (observer == observers[x]) {
                    observers[x] = null; //never shrink array, just leave gaps for later added observers
                    --sz;
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
