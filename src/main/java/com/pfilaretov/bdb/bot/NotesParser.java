package com.pfilaretov.bdb.bot;

import com.pfilaretov.bdb.exception.NoteParseException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for notes
 */
public class NotesParser {

    private static final Logger LOG = LoggerFactory.getLogger(NotesParser.class);

    public List<Note> parse(String text) throws NoteParseException {
        String[] notes = text.split("\\s");
        List<Note> result = new ArrayList<>();

        for (String note : notes) {
            String[] noteAndDuration = note.split("\\.");
            if (noteAndDuration.length != 2) {
                throw new NoteParseException("Wrong note format: " + note);
            }

            String height = noteAndDuration[0].toLowerCase();
            if (!isValidNote(height)) {
                throw new NoteParseException("'" + height + "' note is not supported.");
            }

            byte duration;
            try {
                duration = Byte.parseByte(noteAndDuration[1]);
            } catch (NumberFormatException e) {
                throw new NoteParseException("Cannot parse note duration: " + noteAndDuration[1]);
            }

            if (!isValidDuration(duration)) {
                throw new NoteParseException("'" + duration + "' duration is not supported.");
            }

            result.add(new Note(height, duration));
        }

        return result;
    }

    private boolean isValidNote(String height) {
        return Note.NOTES_SUPPORTED.containsKey(height);
    }

    private boolean isValidDuration(byte duration) {
        return NoteDuration.isSupported(duration);
    }
}
