import java.util.Iterator;
import java.util.function.Predicate;

public class FilteredTextReader implements Iterator<String> {

    ITextReader reader;
    Predicate<String> shouldSkip;
    String nextLine;

    public FilteredTextReader(ITextReader reader, Predicate<String> shouldSkip) {
        this.reader = reader;
        this.shouldSkip = shouldSkip;
        updateNextLine();
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        String result = nextLine;
        updateNextLine();
        return result;
    }

    private void updateNextLine() {
        nextLine = reader.readLine();
        while (nextLine != null && shouldSkip.test(nextLine)) {
            nextLine = reader.readLine();
        }
    }
}