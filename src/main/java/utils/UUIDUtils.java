package utils;

import java.util.UUID;

public class UUIDUtils {

    public static String generateUUIDv4() {
        return UUID.randomUUID().toString();
    }
}
