package com.pfilaretov.bdb.bot;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * represents a note
 */
public class Note {
    // TODO - convert to enum?

    public static final String C1 = "c1";
    public static final String C_SHARP_1 = "c#1";
    public static final String D1 = "d1";
    public static final String D_SHARP_1 = "d#1";
    public static final String E1 = "e1";
    public static final String F1 = "f1";
    public static final String F_SHARP_1 = "f#1";
    public static final String G1 = "g1";
    public static final String G_SHARP_1 = "g#1";
    public static final String A1 = "a1";
    public static final String A_SHARP_1 = "a#1";
    public static final String H1 = "h1";

    public static final byte WHOLE = 1;
    public static final byte HALF = 2;
    public static final byte QUARTER = 4;
    public static final byte EIGHTH = 8;
    public static final byte SIXTEENTH = 16;

    public static final Map<String, String> NOTES_SUPPORTED;
    public static final Set<Byte> DURATIONS_SUPPORTED;

    static {
        // TODO - double-flat/sharp, natural, ...
        // text notation:
        // c1.1 c1#.2 c1b.4 ... h1.1 h1#.2 h1b.2

        // TODO - add more notes
        // using LinkedHashMap to guarantee the order, these lists are used in usage info. Can we make it a better way?
        NOTES_SUPPORTED = new LinkedHashMap<>();
        NOTES_SUPPORTED.put(C1, "media/audio/c1.mp3");
        NOTES_SUPPORTED.put(C_SHARP_1, "media/audio/c#1.mp3");
        NOTES_SUPPORTED.put(D1, "media/audio/d1.mp3");
        NOTES_SUPPORTED.put(D_SHARP_1, "media/audio/d#1.mp3");
        NOTES_SUPPORTED.put(E1, "media/audio/e1.mp3");
        NOTES_SUPPORTED.put(F1, "media/audio/f1.mp3");
        NOTES_SUPPORTED.put(F_SHARP_1, "media/audio/f#1.mp3");
        NOTES_SUPPORTED.put(G1, "media/audio/g1.mp3");
        NOTES_SUPPORTED.put(G_SHARP_1, "media/audio/g#1.mp3");
        NOTES_SUPPORTED.put(A1, "media/audio/a1.mp3");
        NOTES_SUPPORTED.put(A_SHARP_1, "media/audio/a#1.mp3");
        NOTES_SUPPORTED.put(H1, "media/audio/h1.mp3");

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
