package com.pfilaretov.bdb.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for notes
 */
public class NotesParser {

    public List<Note> parse(String text) {
        // text notation:
        // TODO - double-flat/sharp, natural, ...
        // TODO - provide it in a help message
        // c1.1, c1#.2, c1b.4, ..., h1.1, h1#.2, h1b.2

        return new ArrayList<>();
    }
}
