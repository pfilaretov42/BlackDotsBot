package com.pfilaretov.bdb.bot;

/**
 * represents a note
 */
public class Note {
    private final String height;
    private final short duration;

    public Note(String height, short duration) {
        this.height = height;
        this.duration = duration;
    }

    public String getHeight() {
        return height;
    }

    public short getDuration() {
        return duration;
    }
}
