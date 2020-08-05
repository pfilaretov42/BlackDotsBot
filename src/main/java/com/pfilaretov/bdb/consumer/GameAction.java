package com.pfilaretov.bdb.consumer;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Action representing the game
 */
public class GameAction implements Consumer<Update> {

    private static final Logger LOG = LoggerFactory.getLogger(GameAction.class);

    private final SilentSender silent;
    private final MessageSender sender;

    public GameAction(SilentSender silent, MessageSender sender) {
        this.silent = silent;
        this.sender = sender;
    }

    @Override
    public void accept(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String gameShortName = callbackQuery.getGameShortName();
        User from = callbackQuery.getFrom();

        LOG.warn("Starting game {} from {}", gameShortName, from.getFirstName());
    }

}
