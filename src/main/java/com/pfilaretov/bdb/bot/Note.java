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

    public static final String C4 = "c4";
    public static final String C_SHARP_4 = "c#4";
    public static final String D4 = "d4";
    public static final String D_SHARP_4 = "d#4";
    public static final String E4 = "e4";
    public static final String F4 = "f4";
    public static final String F_SHARP_4 = "f#4";
    public static final String G4 = "g4";
    public static final String G_SHARP_4 = "g#4";
    public static final String A4 = "a4";
    public static final String A_SHARP_4 = "a#4";
    public static final String B4 = "b4";

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
        NOTES_SUPPORTED.put(C4, "media/audio/c4.mp3");
        NOTES_SUPPORTED.put(C_SHARP_4, "media/audio/c#4.mp3");
        NOTES_SUPPORTED.put(D4, "media/audio/d4.mp3");
        NOTES_SUPPORTED.put(D_SHARP_4, "media/audio/d#4.mp3");
        NOTES_SUPPORTED.put(E4, "media/audio/e4.mp3");
        NOTES_SUPPORTED.put(F4, "media/audio/f4.mp3");
        NOTES_SUPPORTED.put(F_SHARP_4, "media/audio/f#4.mp3");
        NOTES_SUPPORTED.put(G4, "media/audio/g4.mp3");
        NOTES_SUPPORTED.put(G_SHARP_4, "media/audio/g#4.mp3");
        NOTES_SUPPORTED.put(A4, "media/audio/a4.mp3");
        NOTES_SUPPORTED.put(A_SHARP_4, "media/audio/a#4.mp3");
        NOTES_SUPPORTED.put(B4, "media/audio/b4.mp3");

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
