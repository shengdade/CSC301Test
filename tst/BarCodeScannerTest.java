import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BarCodeScannerTest {

    @Test
    public void initialBarCodeIsNull() {
        BarCodeScanner scanner = new BarCodeScanner();
        assertNull(scanner.getBarCode());
    }

    @Test
    public void canScanBarCode() {
        BarCodeScanner scanner = new BarCodeScanner();
        scanner.scan("A123");
        assertEquals("A123", scanner.getBarCode());
    }


    @Test(expected = NullPointerException.class)
    public void cannotScanNull() {
        BarCodeScanner scanner = new BarCodeScanner();
        scanner.scan(null);
    }


    // TODO: Test the subscribe/unsubscribe methods …
    //       Add your test methods (and any helper classes and/or
    //	   functions you might need) on the next page(s)


    public static class Spy implements Consumer<IScale> {

        List<Long> readings = new LinkedList<>();

        @Override
        public void accept(IScale scale) {
            readings.add(scale.getWeight());
        }

        public List<Long> getReadings() {
            return Collections.unmodifiableList(readings);
        }

    }

    private Scale scale;
    private Spy spy1;
    private Spy spy2;


    @Before
    public void setup() {
        scale = new Scale();
        spy1 = new Spy();
        spy2 = new Spy();
    }

    @Test
    public void listenerIsCalledWithTheCorrectValue() {
        scale.subscribe(spy1);

        scale.setWeight(42);

        List<Long> readings = spy1.getReadings();
        assertEquals(1, readings.size());
        assertEquals(42, readings.get(0).longValue());
    }

    @Test
    public void listenerIsNotCalledWhenSetWeightThrowsAnError() {
        scale.subscribe(spy1);

        try {
            scale.setWeight(-1);
        } catch (IllegalArgumentException e) {
        }

        assertTrue(spy1.getReadings().isEmpty());
    }

    @Test
    public void listenerIsCalledWithTheCorrectValueEveryTime() {
        scale.subscribe(spy1);

        scale.setWeight(42);
        scale.setWeight(23);
        scale.setWeight(71);

        List<Long> readings = spy1.getReadings();

        assertEquals(3, readings.size());
        assertEquals(42, readings.get(0).longValue());
        assertEquals(23, readings.get(1).longValue());
        assertEquals(71, readings.get(2).longValue());
    }

    @Test
    public void unsubscribeListenerIsNotNotified() {
        scale.subscribe(spy1);
        scale.setWeight(42);
        scale.setWeight(23);

        scale.unsubscribe(spy1);
        scale.setWeight(71);

        List<Long> readings = spy1.getReadings();
        assertEquals(2, readings.size());
        assertEquals(42, readings.get(0).longValue());
        assertEquals(23, readings.get(1).longValue());
    }

    @Test
    public void allListenersAreCalledWithTheCorrectValue() {
        scale.subscribe(spy1);
        scale.subscribe(spy2);
        scale.setWeight(42);
        scale.setWeight(23);

        scale.unsubscribe(spy1);
        scale.setWeight(71);

        List<Long> readings = spy1.getReadings();
        assertEquals(2, readings.size());
        assertEquals(42, readings.get(0).longValue());
        assertEquals(23, readings.get(1).longValue());

        readings = spy2.getReadings();
        assertEquals(3, readings.size());
        assertEquals(42, readings.get(0).longValue());
        assertEquals(23, readings.get(1).longValue());
        assertEquals(71, readings.get(2).longValue());
    }

}