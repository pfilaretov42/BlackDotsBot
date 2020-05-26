package com.pfilaretov.bdb.bot;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * represents a note
 */
public class Note {
    // TODO - convert to enum?

    public static final String C1 = "c1";
    public static final String D1 = "d1";
    public static final String E1 = "e1";

    public static final byte WHOLE = 1;
    public static final byte HALF = 2;
    public static final byte QUARTER = 4;
    public static final byte EIGHTH = 8;
    public static final byte SIXTEENTH = 16;

    public static final Set<String> NOTES_SUPPORTED;
    public static final Set<Byte> DURATIONS_SUPPORTED;

    static {
        // TODO - double-flat/sharp, natural, ...
        // text notation:
        // c1.1 c1#.2 c1b.4 ... h1.1 h1#.2 h1b.2

        // TODO - add more to these sets
        // using LinkedHashSet to guarantee the order, these lists are used in usage info. Can we make it a better way?
        NOTES_SUPPORTED = new LinkedHashSet<>();
        NOTES_SUPPORTED.add(C1);
        NOTES_SUPPORTED.add(D1);
        NOTES_SUPPORTED.add(E1);

        DURATIONS_SUPPORTED = new LinkedHashSet<>();
        DURATIONS_SUPPORTED.add(WHOLE);
        DURATIONS_SUPPORTED.add(HALF);
        DURATIONS_SUPPORTED.add(QUARTER);
        DURATIONS_SUPPORTED.add(EIGHTH);
        DURATIONS_SUPPORTED.add(SIXTEENTH);
    }


    private final String height;
    private final byte duration;

    public Note(String height, byte duration) {
        this.height = height;
        this.duration = duration;
    }

    public String getHeight() {
        return height;
    }

    public byte getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return height + "[" + duration + "]";
    }
}
