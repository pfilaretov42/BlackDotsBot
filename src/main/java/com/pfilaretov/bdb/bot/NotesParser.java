package com.pfilaretov.bdb.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for notes
 */
public class NotesParser {

    private static final Logger LOG = LoggerFactory.getLogger(NotesParser.class);

    public List<Note> parse(String text) {
        String[] notes = text.split("\\s");
        LOG.debug("got text splitted: {}", Arrays.toString(notes));

        List<Note> result = new ArrayList<>();

        for (String note : notes) {
            String[] noteAndDuration = note.split("\\.");
            LOG.debug("note splitted: {}", Arrays.toString(noteAndDuration));

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
        return Note.NOTES_SUPPORTED.contains(height);
    }

    private boolean isValidDuration(byte duration) {
        return Note.DURATIONS_SUPPORTED.contains(duration);
    }
}
