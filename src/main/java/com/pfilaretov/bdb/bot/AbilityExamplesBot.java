package com.pfilaretov.bdb.bot;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Abilities examples
 */
@Component
public class AbilityExamplesBot extends AbilityBot {

    private static final Logger LOG = LoggerFactory.getLogger(AbilityExamplesBot.class);

    private static final String BOT_USER_NAME = "BlackDotsBot";

    @Value("${BLACK_DOTS_BOT_CREATOR_ID}")
    private int creatorId;

    public AbilityExamplesBot(@Value("${BLACK_DOTS_BOT_TOKEN}") String token) {
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
        VideoAction action = new VideoAction(sender);
        return Reply.of(action, Flag.TEXT);
    }

    public AbilityExtension testAbilityExtension() {
        return new TestAbilityExtension(silent);
    }

    public Ability sayHelloWorld() {
        return Ability
            .builder()
            .name("hello")
            .info("says hello world!")
            .input(0)
            .locality(ALL)
            .privacy(PUBLIC)
            .action(ctx -> silent.send("Hello world!", ctx.chatId()))
            .post(ctx -> silent.send("Bye world!", ctx.chatId()))
            .build();
    }

    /**
     * Reply on the bot's message ability
     */
    public Ability playWithMe() {
        String playMessage = "Play with me!";

        return Ability.builder()
            .name("play")
            .info("Do you want to play with me?")
            .privacy(PUBLIC)
            .locality(ALL)
            .input(0)
            .action(ctx -> silent.forceReply(playMessage, ctx.chatId()))
            // The signature of a reply is -> (Consumer<Update> action, Predicate<Update>... conditions)
            // So, we  first declare the action that takes an update (NOT A MESSAGECONTEXT) like the action above
            // The reason of that is that a reply can be so versatile depending on the message, context becomes an inefficient wrapping
            .reply(upd -> {
                    // Prints to console
                    LOG.info("I'm in a reply!");
                    // Sends message
                    silent.send("It's been nice playing with you!", upd.getMessage().getChatId());
                },
                // Now we start declaring conditions, MESSAGE is a member of the enum Flag class
                // That class contains out-of-the-box predicates for your replies!
                // MESSAGE means that the update must have a message
                Flag.MESSAGE,
                // REPLY means that the update must be a reply
                Flag.REPLY,
                // A new predicate user-defined
                // The reply must be to the bot
                isReplyToBot(),
                // If we process similar logic in other abilities, then we have to make this reply specific to this message
                // The reply is to the playMessage
                isReplyToMessage(playMessage)
            )
            // You can add more replies by calling .reply(...)
            .build();
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().equalsIgnoreCase(message);
        };
    }

    private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
    }

    /**
     * A reply that says "yuck" to all images sent to the bot.
     */
    public Reply sayYuckOnImage() {
        // getChatId is a public utility function in rg.telegram.abilitybots.api.util.AbilityUtils
        Consumer<Update> action = upd -> silent.send("Yuck", getChatId(upd));

        return Reply.of(action, Flag.PHOTO);
    }

    public Reply replyWithVideoOnVideo() {
        Consumer<Update> action = update -> {
            String fileId = update.getMessage().getVideo().getFileId();
            SendVideo message = new SendVideo()
                .setChatId(update.getMessage().getChatId())
                .setVideo(fileId);

            try {
                sender.sendVideo(message);
            } catch (TelegramApiException e) {
                LOG.error("Cannot send video", e);
            }
        };

        return Reply.of(action, update -> update.hasMessage() && update.getMessage().hasVideo());
    }

    public ReplyFlow directMe() {
        Reply saidLeft = Reply.of(upd -> silent.send("Sir, I have gone left.", getChatId(upd)),
            hasMessageWith("left"));

        Reply saidRight = Reply.of(upd -> silent.send("Sir, I have gone right.", getChatId(upd)),
            hasMessageWith("right"));

        // We instantiate a ReplyFlow builder with our internal db (DBContext instance) passed
        // State is always preserved in the db of the bot and remains even after termination
        return ReplyFlow.builder(db)
            // Just like replies, a ReplyFlow can take an action, here we want to send a
            // statement to prompt the user for directions!
            .action(upd -> silent.send("Command me to go left or right!", getChatId(upd)))
            // We should only trigger this flow when the user says "wake up"
            .onlyIf(hasMessageWith("wake up"))
            // The next method takes in an object of type Reply.
            // Here we chain our replies together
            .next(saidLeft)
            // We chain one more reply, which is when the user commands your bot to go right
            .next(saidRight)
            // Finally, we build our ReplyFlow
            .build();
    }

    private Predicate<Update> hasMessageWith(String msg) {
        return upd -> upd.getMessage().getText().equalsIgnoreCase(msg);
    }

    /**
     * Use the database to fetch a count per user and increments.
     * <p>
     * Use /count to experiment with this ability.
     */
    public Ability useDatabaseToCountPerUser() {
        return Ability.builder()
            .name("count")
            .info("Increments a counter per user")
            .privacy(PUBLIC)
            .locality(ALL)
            .input(0)
            .action(ctx -> {
                // db.getMap takes in a string, this must be unique and the same everytime you want to call the exact same map
                // TODO: Using integer as a key in this db map is not recommended, it won't be serialized/deserialized properly if you ever decide to recover/backup db
                Map<String, Integer> countMap = db.getMap("COUNTERS");
                int userId = ctx.user().getId();

                // Get and increment counter, put it back in the map
                Integer counter = countMap.compute(String.valueOf(userId), (id, count) -> count == null ? 1 : ++count);

                // Send formatted will enable markdown
                String message = String.format("%s, your count is now *%d*!", ctx.user().getUserName(), counter);
                silent.send(message, ctx.chatId());
            })
            .build();
    }
}
