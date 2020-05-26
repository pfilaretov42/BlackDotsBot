package com.pfilaretov.bdb.bot;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;

/**
 * Black Dots Ability bot
 */
@Component
public class BlackDotsAbility extends AbilityBot {

    private static final Logger LOG = LoggerFactory.getLogger(BlackDotsAbility.class);
    private static final String BOT_USER_NAME = "BlackDotsBot";

    @Value("${BLACK_DOTS_BOT_CREATOR_ID}")
    private int creatorId;

    public BlackDotsAbility(@Value("${BLACK_DOTS_BOT_TOKEN}") String token) {
        super(token, BOT_USER_NAME);
    }

    @Override
    public int creatorId() {
        return creatorId;
    }

    // TODO - manage standard ability commands like /commands, /report, etc

    /**
     * Prints usage information on /start command
     */
    public Ability printUsage() {
        String usageMessage = "I can create a music video out of a simple text. "
            + "Just send me a message with a space delimited list of notes in a format of "
            + "[note name].[note duration], for instance:\n"
            // TODO - give Bach D-minior here
            + "c1.4 d1.8 e1.8 c1.4\n"
            + "Supported note names: " + String.join(", ", Note.NOTES_SUPPORTED) + ".\n"
            + "Supported note durations: "
            + Note.DURATIONS_SUPPORTED.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
            + ".";

        return Ability
            .builder()
            .name("start")
            .info("prints usage information")
            .input(0)
            .locality(ALL)
            .privacy(PUBLIC)
            .action(ctx -> silent.send(usageMessage, ctx.chatId()))
            .build();
    }

    /**
     * A video reply to text
     */
    public Reply blackDotsFromText() {
        // Cannot make them beans because this method is called from super constructor
        NotesParser notesParser = new NotesParser();
        VideoMaker videoMaker = new VideoMaker();

        VideoAction action = new VideoAction(sender, notesParser, videoMaker);
        return Reply.of(action, update ->
            Flag.MESSAGE.test(update) && update.getMessage().hasText()
                && !update.getMessage().getText().startsWith("/"));
    }

}
