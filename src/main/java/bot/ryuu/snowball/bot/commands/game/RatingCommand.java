package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class RatingCommand extends AbstractCommand {
    private Map<String, RatingBook> time = new HashMap<>();

    public RatingCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_rating_command");
        setCommandData(
                Commands.slash("rating", "server rating")
                .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        ArrayList<RatingBook.ItemSelect> items = new ArrayList<>();

        Objects.requireNonNull(slash.getGuild()).loadMembers(member -> {
            Optional<Player> player = getPlayer(member.getId(), slash.getGuild().getId());

            player.ifPresent(value -> items.add(new RatingBook.ItemSelect(member.getUser().getName(), value.getScore())));
        }).onSuccess(ignored -> {
            RatingBook book = new RatingBook(items, 2);

            time.put(slash.getGuild().getId(), book);

            slash.deferReply(true).setEmbeds(
                    Theme.getMainEmbed()
                            .setTitle(
                                    Language.message("rating-server", getLanguage(slash))
                            )
                            .setThumbnail(slash.getGuild().getIconUrl())
                            .setDescription(String.valueOf(book.getCurrent())).build()
            ).addActionRow(
                    Button.secondary(getCode() + "_back", "back")
                            .withDisabled(!book.isBackPage()),
                    Button.secondary(getCode() + "_next", "next")
                            .withDisabled(!book.isNextPage())
            ).queue();
        });
    }

    @Override
    protected void buttonInteraction(ButtonInteractionEvent button) {
        super.buttonInteraction(button);

        switch (button.getButton().getId()) {
            case "_rating_command_next" -> buttonIncrementPage(button, 1);
            case "_rating_command_back" -> buttonIncrementPage(button, -1);
        }
    }

    private void buttonIncrementPage(ButtonInteractionEvent button, int increment) {
        RatingBook book = time.get(button.getGuild().getId());

        if (book != null) {
            book.incrementPage(increment);

            button.deferEdit().setEmbeds(
                    Theme.getMainEmbed()
                            .setTitle(
                                    Language.message("rating-server", getLanguage(button))
                            )
                            .setThumbnail(button.getGuild().getIconUrl())
                            .setDescription(String.valueOf(book.getCurrent())).build()
            ).setActionRow(
                    Button.secondary(getCode() + "_back", "back")
                            .withDisabled(!book.isBackPage()),
                    Button.secondary(getCode() + "_next", "next")
                            .withDisabled(!book.isNextPage())
            ).queue();
        } else
            replyError(button);
    }

    @Getter
    @Setter
    private static class RatingBook {
        private final ArrayList<String> pages;
        private int current;

        private final int INCREMENT_PAGE = 1;

        public RatingBook(ArrayList<ItemSelect> items, int sizePage) {
            items.sort(ItemSelect::compare);
            this.pages = new ArrayList<>();
            this.current = 0;

            StringBuilder page = new StringBuilder();

            int size = 1;
            for (int id = 0; id < items.size(); id++) {
                String index = (id == 0) ? ThemeEmoji.KING.getEmoji().getAsMention() : (id + 1) + ". ";
                page.append(index)
                        .append(items.get(id).name())
                        .append(" - ")
                        .append(items.get(id).point())
                        .append("\n");

                if (size == sizePage) {
                    pages.add(page.toString());
                    page.delete(0, page.length());
                    size = 0;
                }
                size++;
            }

            if (!page.toString().equals(""))
                pages.add(page.toString());
        }

        public String getPage(int index) {
            if (index >= 0 && index < pages.size())
                return pages.get(index);
            else
                return null;
        }

        public void incrementPage(int value) {
            if (value < 0 && isBackPage())
                this.current -= INCREMENT_PAGE;
            else if (isNextPage())
                this.current += INCREMENT_PAGE;
        }

        public boolean isNextPage() {
            return this.current + INCREMENT_PAGE < pages.size();
        }

        public boolean isBackPage() {
            return this.current - INCREMENT_PAGE >= 0;
        }

        public record ItemSelect(String name, int point) {
            public static int compare(ItemSelect o1, ItemSelect o2) {
                return (o1.point < o2.point) ? 1 : -1;
            }
        }
    }
}
