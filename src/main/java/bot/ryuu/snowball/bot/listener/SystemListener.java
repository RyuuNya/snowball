package bot.ryuu.snowball.bot.listener;

import bot.ryuu.snowball.data.DataCluster;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@AllArgsConstructor
public class SystemListener extends ListenerAdapter {
    private DataCluster dataCluster;

}