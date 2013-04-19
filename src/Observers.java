import java.util.Observable;
import java.util.Observer;

public interface Observers {
    void call(Observable observable, Object arg);

    void add(Observer observer);

    void remove(Observer observer);

    /**
     * @return a string containing the number of times each listener was called and how often it was first.
     */
    String toString();
}
