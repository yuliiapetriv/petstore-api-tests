package utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    private DataGenerator() {
    }

    public static int generateRandomId() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }

    public static String generateCurrentIsoDate() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

}
