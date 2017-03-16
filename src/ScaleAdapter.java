import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class ScaleAdapter {


    private long prevWeight;
    private Set<BiConsumer<Long, Long>> increaseListeners;
    private Set<BiConsumer<Long, Long>> decreaseListeners;

    public ScaleAdapter(IScale scale) {
        this.prevWeight = scale.getWeight();
        this.increaseListeners = new HashSet<BiConsumer<Long, Long>>();
        this.decreaseListeners = new HashSet<BiConsumer<Long, Long>>();

        scale.subscribe(s -> {
            if (prevWeight < s.getWeight()) {
                increaseListeners.forEach(x -> x.accept(prevWeight, s.getWeight()));
            } else if (prevWeight > s.getWeight()) {
                decreaseListeners.forEach(x -> x.accept(prevWeight, s.getWeight()));
            }

            prevWeight = s.getWeight();
        });

    }


    public void onIncrease(BiConsumer<Long, Long> listener) {
        increaseListeners.add(listener);
    }

    public void onDecrease(BiConsumer<Long, Long> listener) {
        decreaseListeners.add(listener);
    }

    public static void main(String[] args) {
        Scale scale = new Scale();
        ScaleAdapter adapter = new ScaleAdapter(scale);

        adapter.onIncrease((before, after) -> {
            System.out.println("UP from " + before + " to " + after);
        });
        adapter.onDecrease((before, after) -> {
            System.out.println("DOWN from " + before + " to " + after);
        });

        scale.setWeight(10);
        // The following is printed:
        // UP from 0 to 10

        scale.setWeight(20);
        // The following is printed:
        // UP from 10 to 20

        scale.setWeight(20);
        // Nothing is printed (because the weight did not change)

        scale.setWeight(0);
        // DOWN from 20 to 0

    }
}