import java.util.function.Consumer;

public interface IScale {

    // Return the current weight of this scale
    public long getWeight();

    // Subscribe the given listener to this scale.
    // The listener will be called (with this scale as its arguments)
    // whenever this scale's weight is set.
    public void subscribe(Consumer<IScale> listener);

    // Unsubscribe the given listener from this scale
    public void unsubscribe(Consumer<IScale> listener);
}