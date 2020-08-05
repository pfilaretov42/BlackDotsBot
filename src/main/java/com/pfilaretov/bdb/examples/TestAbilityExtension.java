package com.pfilaretov.bdb.examples;

import java.util.function.Consumer;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Ability extension example
 */
public class TestAbilityExtension implements AbilityExtension {

    private SilentSender silent;

    public TestAbilityExtension(SilentSender silent) {
        this.silent = silent;
    }

    public Ability generateVideo() {
        return Ability.builder()
            .name("text")
            .info("Provide text and I'll echo it to you!")
            .privacy(Privacy.PUBLIC)
            .locality(Locality.ALL)
            .input(0)
            .action(generateResponse())
            .build();
    }

    private Consumer<MessageContext> generateResponse() {
        return ctx -> {
            Update update = ctx.update();
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                silent.send("You said: " + text, ctx.chatId());
            }
        };
    }

}
