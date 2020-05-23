package com.pfilaretov.bdb.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
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

    /**
     * A reply to text
     */
    public Reply blackDotsFromText() {
        // Cannot make them beans because this method is called from super constructor
        NotesParser notesParser = new NotesParser();
        VideoMaker videoMaker = new VideoMaker();

        VideoAction action = new VideoAction(sender, notesParser, videoMaker);
        return Reply.of(action, Flag.TEXT);
    }

}
