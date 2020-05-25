package com.pfilaretov.bdb.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for notes
 */
public class NotesParser {

    private static final Logger LOG = LoggerFactory.getLogger(NotesParser.class);

    // TODO - move these to Note class?
    private final Set<String> validNotes;
    private final Set<Byte> validDurations;

    public NotesParser() {
        validNotes = new HashSet<>();
        validNotes.add(Note.C1);
        validNotes.add(Note.D1);
        validNotes.add(Note.E1);

        validDurations = new HashSet<>();
        validDurations.add(Note.WHOLE);
        validDurations.add(Note.HALF);
        validDurations.add(Note.QUARTER);
        validDurations.add(Note.EIGHTH);
        validDurations.add(Note.SIXTEENTH);
    }

    public List<Note> parse(String text) {
        // text notation:
        // TODO - double-flat/sharp, natural, ...
        // TODO - provide it in a help message
        // c1.1 c1#.2 c1b.4 ... h1.1 h1#.2 h1b.2

        String[] notes = text.split("\\s");
        LOG.info("got text splitted: {}", Arrays.toString(notes));

        List<Note> result = new ArrayList<>();

        for (String note : notes) {
            String[] noteAndDuration = note.split("\\.");
            LOG.info("note splitted: {}", Arrays.toString(noteAndDuration));

            if (noteAndDuration.length != 2) {
                // TODO - return error, print help
                LOG.warn("wrong note format: {}", noteAndDuration);
                continue;
            }

            String height = noteAndDuration[0].toLowerCase();

            if (!isValidNote(height)) {
                // TODO - return error, print help
                LOG.warn("note {} is invalid", height);
                continue;
            }

            byte duration;
            try {
                duration = Byte.parseByte(noteAndDuration[1]);
            } catch (NumberFormatException e) {
                // TODO - return error, print help
                LOG.warn("cannot parse note duration: {}", noteAndDuration[1]);
                continue;
            }

            if (!isValidDuration(duration)) {
                // TODO - return error, print help
                LOG.warn("{} duration is not supported", duration);
                continue;
            }

            result.add(new Note(height, duration));
        }

        return result;
    }

    private boolean isValidNote(String height) {
        return validNotes.contains(height);
    }

    private boolean isValidDuration(byte duration) {
        return validDurations.contains(duration);
    }
}
