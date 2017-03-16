import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class BarCodeScanner implements IBarCodeScanner {

    Set<Consumer<IBarCodeScanner>> listeners = new HashSet<>();
    private String barCode;


    public void scan(String barCode) {
        Objects.requireNonNull(barCode);
        this.barCode = barCode;
        listeners.forEach(listener -> listener.accept(this));
    }

    @Override
    public void subscribe(Consumer<IBarCodeScanner> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(Consumer<IBarCodeScanner> listener) {
        listeners.remove(listener);
    }

    @Override
    public String getBarCode() {
        return barCode;
    }

}