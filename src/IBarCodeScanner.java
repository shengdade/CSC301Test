import java.util.function.Consumer;

public interface IBarCodeScanner {

    // Return the most recently scanned bar-code.
    public String getBarCode();

    // Subscribe the given listener to this scanner.
    // The listener will be called (with this scanner as its arguments)
    // whenever a barcode is scanned by this scanner.
    public void subscribe(Consumer<IBarCodeScanner> listener);

    // Unsubscribe the given listener from this scanner.
    public void unsubscribe(Consumer<IBarCodeScanner> listener);
}