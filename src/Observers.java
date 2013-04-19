import java.util.Observable;
import java.util.Observer;

public interface Observers {
    void call(Observable observable, Object arg);

    void add(Observer observer);

    void remove(Observer observer);
}
