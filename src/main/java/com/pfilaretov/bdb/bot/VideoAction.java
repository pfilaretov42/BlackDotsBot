package com.pfilaretov.bdb.bot;

import java.io.File;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Replies with video on text message
 */
public class VideoAction implements Consumer<Update> {

    private static final Logger LOG = LoggerFactory.getLogger(VideoAction.class);

    private MessageSender sender;

    VideoAction(MessageSender sender) {
        this.sender = sender;
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

        File file = new File("VID_123.mp4");

        SendVideo video = new SendVideo();
        video.setChatId(AbilityUtils.getChatId(update));
        video.setVideo(file);

        SendVideoNote videoNote = new SendVideoNote();
        videoNote.setChatId(AbilityUtils.getChatId(update));
        videoNote.setVideoNote(file);

        try {
//            sender.sendVideo(video);
            sender.sendVideoNote(videoNote);
        } catch (TelegramApiException e) {
            LOG.error("Cannot send video", e);
        }
    }
}
