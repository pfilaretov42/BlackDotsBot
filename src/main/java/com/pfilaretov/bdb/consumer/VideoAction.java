package com.pfilaretov.bdb.consumer;

import com.pfilaretov.bdb.bot.BlackDotsAbility;
import com.pfilaretov.bdb.note.Note;
import com.pfilaretov.bdb.note.NotesParser;
import com.pfilaretov.bdb.media.VideoMaker;
import com.pfilaretov.bdb.exception.NoteParseException;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
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

    private final SilentSender silent;
    private final MessageSender sender;
    private final NotesParser notesParser;
    private final VideoMaker videoMaker;

    public VideoAction(SilentSender silent, MessageSender sender, NotesParser notesParser,
        VideoMaker videoMaker) {
        this.silent = silent;
        this.sender = sender;
        this.notesParser = notesParser;
        this.videoMaker = videoMaker;
    }

    @Override
    public void accept(Update update) {
        File file = null;
        Long chatId = AbilityUtils.getChatId(update);
        String text = update.getMessage().getText();

        try {
            List<Note> notes = notesParser.parse(text);

            // TODO - Too many requests in a second Telegram API restriction?
            /// generating a video can take time, so send a confirmation message first
            SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText("Give me a second...");
            sender.execute(message);

            file = videoMaker.generateVideo(notes);

            SendVideo video = new SendVideo();
            video.setChatId(chatId);
            video.setVideo(file);

            sender.sendVideo(video);
        } catch (NoteParseException e) {
            silent.send(e.getMessage() + "\n" + BlackDotsAbility.USAGE_MESSAGE, chatId);
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
