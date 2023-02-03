package com.highbridge.file.util;

import java.util.UUID;

public class UUIDGenerator {

    /**
     * Converts UUID to string and returns it.
     *
     * @return String UUID
     */
    public String getUUID() {
        return String.valueOf(UUID.randomUUID());
    }
}
