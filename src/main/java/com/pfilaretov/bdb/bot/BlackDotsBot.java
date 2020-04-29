package com.pfilaretov.bdb.bot;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * The main Bot class
 */
//@Component
public class BlackDotsBot extends TelegramLongPollingBot {

    private static final String BOT_USER_NAME = "BlackDotsBot";

    @Value("${BLACK_DOTS_BOT_TOKEN}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        // check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Create a SendMessage object with mandatory fields
            SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());
            try {
                // Call method to send the message
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        // If it is a photo
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Array with photos
            List<PhotoSize> photos = update.getMessage().getPhoto();

            // Get largest photo's file_id and send it back
            String fileId = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(() -> new RuntimeException("Cannot obtain file size")).getFileId();
            SendPhoto msg = new SendPhoto()
                .setChatId(update.getMessage().getChatId())
                .setPhoto(fileId)
                .setCaption("Photo");
            try {
                // Call method to send the photo
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
