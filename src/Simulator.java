public class Simulator {

    private Scale scale = new Scale();
    private BarCodeScanner scanner = new BarCodeScanner();


    public IScale getScale() {
        return scale;
    }

    public IBarCodeScanner getScanner() {
        return scanner;
    }


    // Pre-condition: logEntry is one of the following:
    //    1. "SCAN:" followed by some (non-empty) string
    //    2. "WEIGHT:" followed by some (non-negative) number
    public void simulate(String logEntry) {
        if (logEntry.startsWith("SCAN:")) {
            String barCode = logEntry.substring(5);
            this.scanner.scan(barCode);
        } else {
            long weight = Long.parseLong(logEntry.substring(7));
            this.scale.setWeight(weight);
        }
    }
}