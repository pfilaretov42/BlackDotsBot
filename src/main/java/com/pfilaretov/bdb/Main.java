package com.pfilaretov.bdb;

import com.pfilaretov.bdb.bot.BlackDotsBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Main program to register the Bot
 */
public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BlackDotsBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
