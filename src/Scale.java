import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Scale implements IScale {

    private long weight;
    private Set<Consumer<IScale>> listeners;


    public Scale() {
        this.listeners = new HashSet<>();
    }


    @Override
    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
        listeners.forEach(listener -> listener.accept(this));
    }


    @Override
    public void subscribe(Consumer<IScale> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(Consumer<IScale> listener) {
        listeners.remove(listener);
    }

}