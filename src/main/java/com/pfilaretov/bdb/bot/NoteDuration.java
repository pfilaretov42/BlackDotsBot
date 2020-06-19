package com.pfilaretov.bdb.bot;

import java.util.HashSet;
import java.util.Set;

/**
 * Note duration enum
 */
public enum NoteDuration {
    WHOLE("whole", (byte) 1),
    HALF("half", (byte) 2),
    QUARTER("quarter", (byte) 4),
    EIGHTH("eighth", (byte) 8),
    SIXTEENTH("sixteenth", (byte) 16);

    private static final Set<Byte> DURATIONS_SUPPORTED = new HashSet<>();

    static {
        for (NoteDuration noteDuration : NoteDuration.values()) {
            DURATIONS_SUPPORTED.add(noteDuration.getDuration());
        }
    }

    private final String name;
    private final byte duration;

    NoteDuration(String name, byte duration) {
        this.name = name;
        this.duration = duration;
    }

    public byte getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return duration + " - " + name;
    }

    public static boolean isSupported(byte duration) {
        return DURATIONS_SUPPORTED.contains(duration);
    }
}
