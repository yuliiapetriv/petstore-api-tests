package utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    private DataGenerator() {
    }

    public static int generateRandomId() {
        long timestamp = System.currentTimeMillis() % 100000;
        int randomPart = ThreadLocalRandom.current().nextInt(1, 100);
        return (int) (timestamp * 100 + randomPart);
    }

    public static String generateCurrentIsoDate() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

}
