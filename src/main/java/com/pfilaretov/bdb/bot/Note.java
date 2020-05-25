package com.pfilaretov.bdb.bot;

/**
 * represents a note
 */
public class Note {
    // TODO - convert to enum?

    // TODO - add notes
    public static final String C1 = "c1";
    public static final String D1 = "d1";
    public static final String E1 = "e1";

    public static final byte WHOLE = 1;
    public static final byte HALF = 2;
    public static final byte QUARTER = 4;
    public static final byte EIGHTH = 8;
    public static final byte SIXTEENTH = 16;

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
