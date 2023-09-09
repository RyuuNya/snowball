package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeMessage;
import lombok.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

@Getter
@Setter
public class RatingCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    private Map<String, RatingBook> time = new HashMap<>();

    public RatingCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_rating_command");
        setCommandData(Commands.slash("rating", "server rating")
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        ArrayList<RatingBook.ItemSelect> items = new ArrayList<>();

        Objects.requireNonNull(event.getGuild()).loadMembers(member -> {
            Optional<Player> player = playerRepository.findById(member.getId());

            player.ifPresent(value -> items.add(new RatingBook.ItemSelect(member.getUser().getName(), value.getScore())));
        }).onSuccess(ignored -> {
            RatingBook book = new RatingBook(items, 2);

            time.put(event.getGuild().getId(), book);

            event.deferReply(true).setEmbeds(
                    ThemeMessage.getMainEmbed()
                            .setTitle("Rating server")
                            .setDescription(book.getCurrentPage()).build()
            ).addActionRow(
                    Button.secondary(getCode() + "_back", "back")
                            .withDisabled(!book.isBackPage()),
                    Button.secondary(getCode() + "_next", "next")
                            .withDisabled(!book.isNextPage())
            ).queue();
        });
    }

    @Override
    protected void buttonCommandInteraction(ButtonInteractionEvent event) {
        switch (event.getButton().getId()) {
            case "_rating_command_next" -> buttonIncrementPage(event, 1);
            case "_rating_command_back" -> buttonIncrementPage(event, -1);
        }
    }

    private void buttonIncrementPage(ButtonInteractionEvent event, int increment) {
        RatingBook book = time.get(event.getGuild().getId());

        if (book != null) {
            book.incrementPage(increment);

            event.deferEdit().setEmbeds(
                    ThemeMessage.getMainEmbed()
                            .setTitle("Rating server")
                            .setDescription(book.getCurrentPage()).build()
            ).setActionRow(
                    Button.secondary(getCode() + "_back", "back")
                            .withDisabled(!book.isBackPage()),
                    Button.secondary(getCode() + "_next", "next")
                            .withDisabled(!book.isNextPage())
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getErrorEmbed()
                            .setDescription("the request has been out of date for a long time").build()
            ).queue();
    }

    @Override
    protected void stringSelectCommandInteraction(StringSelectInteractionEvent event) {

    }

    @Override
    protected void entitySelectCommandInteraction(EntitySelectInteractionEvent event) {

    }

    @Override
    protected void modalCommandInteraction(ModalInteractionEvent event) {

    }

    @Getter
    @Setter
    private static class RatingBook {
        private final ArrayList<String> pages;
        private int currentPage;

        private final int INCREMENT_PAGE = 1;

        public RatingBook(ArrayList<ItemSelect> items, int sizePage) {
            Collections.sort(items, ItemSelect::compare);
            this.pages = new ArrayList<>();
            this.currentPage = 0;

            String page = "";

            int size = 1;
            for (int id = 0; id < items.size(); id++) {
                page += (id + 1) + ". " + items.get(id).name + " - " + items.get(id).point + "\n";

                if (size == sizePage) {
                    pages.add(page);
                    page = "";
                    size = 0;
                }
                size++;
            }

            if (!page.equals(""))
                pages.add(page);
        }

        public String getPage(int index) {
            if (index >= 0 && index < pages.size())
                return pages.get(index);
            else
                return null;
        }

        public String getCurrentPage() {
            return pages.get(currentPage);
        }

        public void incrementPage(int value) {
            if (value < 0 && isBackPage())
                this.currentPage -= INCREMENT_PAGE;
            else if (isNextPage())
                this.currentPage += INCREMENT_PAGE;
        }

        public boolean isNextPage() {
            return this.currentPage + INCREMENT_PAGE < pages.size();
        }

        public boolean isBackPage() {
            return this.currentPage - INCREMENT_PAGE >= 0;
        }

        public  record ItemSelect(String name, int point) {
            public static int compare(ItemSelect o1, ItemSelect o2) {
                return (o1.point < o2.point) ? 1 : -1;
            }
        }
    }
}
