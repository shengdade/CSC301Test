import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class App {

    private Simulator simulator;
    private Map<String, Long> barCode2weight;


    public App(Simulator simulator,
               Runnable onNewPaintMix,
               BiConsumer<String, Long> onPaintAdded,
               Consumer<Map<String, Long>> onPaintMixReady) {

        this.simulator = simulator;

        // Use the ScaleAdapter from question 2
        ScaleAdapter weightChange = new ScaleAdapter(simulator.getScale());
        // To cleanly define the logic of the app:

        weightChange.onIncrease((before, after) -> {
            if (barCode2weight == null) {
                this.barCode2weight = new HashMap<>();
                onNewPaintMix.run();
            } else {
                long delta = after - before;
                String barCode = simulator.getScanner().getBarCode();
                onPaintAdded.accept(barCode, delta);

                long total = barCode2weight.getOrDefault(barCode, 0L) + delta;
                barCode2weight.put(barCode, total);

            }
        });

        weightChange.onDecrease((before, after) -> {
            onPaintMixReady.accept(getPaintMix());
            this.barCode2weight = null;
        });
    }


    public void run(ITextReader logFileReader) {
        // Use the iterator from question 2
        Iterator<String> logEntries = new FilteredTextReader(
                logFileReader, s -> s.trim().isEmpty() || s.startsWith("#"));

        while (logEntries.hasNext()) {
            this.simulator.simulate(logEntries.next());
        }
    }


    public Map<String, Long> getPaintMix() {
        return Collections.unmodifiableMap(barCode2weight);
    }

    public static void main(String[] args) throws FileNotFoundException {

        Runnable onNewPaintMix = () -> {
            System.out.println("Start new paint-mix");
        };

        BiConsumer<String, Long> onPaintAdded = (barCode, weight) -> {
            System.out.println("Added " + weight + " ml of " + barCode);
        };

        Consumer<Map<String, Long>> onPaintMixReady = (mix) -> {
            System.out.println("Paint-mix: " + mix + "\n");
        };

        App app = new App(new Simulator(),
                onNewPaintMix, onPaintAdded, onPaintMixReady);


        // Open the file that was provided as a command-line argument
        InputStream inputFile = new FileInputStream(new File(args[0]));

        // Create a reader
        // ITextReader reader = new SimpleTextReader(inputFile);

        // Run the application
        // app.run(reader);
    }

}