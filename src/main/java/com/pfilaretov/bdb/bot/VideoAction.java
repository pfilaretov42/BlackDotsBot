package com.pfilaretov.bdb.bot;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Replies with video on text message
 */
public class VideoAction implements Consumer<Update> {

    private static final Logger LOG = LoggerFactory.getLogger(VideoAction.class);

    private final MessageSender sender;
    private final NotesParser notesParser;
    private final VideoMaker videoMaker;

    VideoAction(MessageSender sender, NotesParser notesParser, VideoMaker videoMaker) {
        this.sender = sender;
        this.notesParser = notesParser;
        this.videoMaker = videoMaker;
    }

    @Override
    public void accept(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            return;
        }

        List<Note> notes = notesParser.parse(text);
        File file = videoMaker.generateVideo(notes);

        SendVideo video = new SendVideo();
        video.setChatId(AbilityUtils.getChatId(update));
        video.setVideo(file);

        try {
            sender.sendVideo(video);

            // TODO - delete file
        } catch (TelegramApiException e) {
            LOG.error("Cannot send video", e);
        }
    }
}
