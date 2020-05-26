package com.pfilaretov.bdb.bot;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        String text = update.getMessage().getText();
        File file = null;
        try {
            Long chatId = AbilityUtils.getChatId(update);

            // TODO - Too many requests in a second Telegram API restriction?
            /// generating a video can take time, so send a confirmation message first
            SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Give me a second...");
            sender.execute(message);

            // generate and send a video
            List<Note> notes = notesParser.parse(text);
            file = videoMaker.generateVideo(notes);

            SendVideo video = new SendVideo();
            video.setChatId(chatId);
            video.setVideo(file);

            sender.sendVideo(video);
        } catch (TelegramApiException e) {
            LOG.error("Cannot send video", e);
        } finally {
            deleteFile(file);
        }
    }

    private void deleteFile(File file) {
        if (file != null && !file.delete()) {
            LOG.warn("Cannot delete file {}", file.getName());
        }
    }
}
