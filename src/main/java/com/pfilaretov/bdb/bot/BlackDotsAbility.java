package com.pfilaretov.bdb.bot;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import com.pfilaretov.bdb.consumer.GameAction;
import com.pfilaretov.bdb.consumer.VideoAction;
import com.pfilaretov.bdb.media.VideoMaker;
import com.pfilaretov.bdb.note.Note;
import com.pfilaretov.bdb.note.NoteDuration;
import com.pfilaretov.bdb.note.NotesParser;
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
    public static final String USAGE_MESSAGE;

    static {
        StringBuilder usageMsg = new StringBuilder("I can create a music video out of a simple text. ")
            .append("Just send me a message with a space delimited list of notes in a format of ")
            .append("[note name].[note duration], for instance:\n")
            .append("c4.4 f4.8 g4.8 c4.2\n")
            .append("Supported note names: ")
            .append(String.join(", ", Note.NOTES_SUPPORTED.keySet())).append(".\n")
            .append("Supported note durations:\n");

        for (NoteDuration noteDuration : NoteDuration.values()) {
            usageMsg.append(noteDuration).append("\n");
        }

        USAGE_MESSAGE = usageMsg.toString();
    }


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
        return Ability
            .builder()
            .name("start")
            .info("prints usage information")
            .input(0)
            .locality(ALL)
            .privacy(PUBLIC)
            .action(ctx -> silent.send(USAGE_MESSAGE, ctx.chatId()))
            .build();
    }

    /**
     * A video reply to text
     */
    public Reply blackDotsFromText() {
        // Cannot make them beans because this method is called from super constructor
        NotesParser notesParser = new NotesParser();
        VideoMaker videoMaker = new VideoMaker();

        VideoAction action = new VideoAction(silent, sender, notesParser, videoMaker);
        return Reply.of(action, update ->
            Flag.MESSAGE.test(update) && update.getMessage().hasText()
                && !update.getMessage().getText().startsWith("/"));
    }

    /**
     * A game callback
     */
    public Reply gameCallback() {
        GameAction action = new GameAction(silent, sender);
        return Reply.of(action, update ->
            Flag.CALLBACK_QUERY.test(update)
                && update.getCallbackQuery().getGameShortName() != null
                && !update.getCallbackQuery().getGameShortName().isEmpty()
        );
    }

}
