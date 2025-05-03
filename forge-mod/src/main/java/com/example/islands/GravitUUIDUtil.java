package com.example.islands;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GravitUUIDUtil {

    public static UUID getOfflineUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }
}
