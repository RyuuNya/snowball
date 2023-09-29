package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;

public class BookCommand extends CommandAbstract {
    public BookCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_book_command");
        setCommand(
                Commands.slash("book", "book")
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        slash.getGuild().loadMembers(member -> {

        });
    }

    private static class Book {
        private ArrayList<String> pages;
        private int current;

        public Book(ArrayList<Entry> entries, int pageSize) {
            this.pages = new ArrayList<>();
            this.current = 0;

            entries.sort(Entry::compareTo);

            int id = 0;
            StringBuilder page = new StringBuilder();
            for (Entry e : entries) {
                page.append(id)
                        .append(". - ")
                        .append(e.name())
                        .append(" - ")
                        .append(e.score())
                        .append("\n");

                if (id + 1 == pageSize) {
                    id = 0;
                    pages.add(page.toString());
                    page.delete(0, page.length());
                }
                id++;
            }

            if (!page.toString().equals(""))
                pages.add(page.toString());
        }

        public String page(int value) {
            if (value < 0 || value >= pages.size())
                return "null";
            else
                return pages.get(value);
        }

        public String page() {
            return pages.get(current);
        }

        public void overfly(int increment) {
            if (increment >= 0 && current + 1 < pages.size())
                current++;
            else if (increment < 0 && current - 1 >= 0)
                current--;
        }
    }

    private record Entry(String name, int score) {
        public static int compareTo(Entry a, Entry b) {
            return (a.score > b.score) ? 1 : -1;
        }
    }
}
