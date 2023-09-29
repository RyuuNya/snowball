package bot.ryuu.snowball.data.server;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.tools.language.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    private String id;

    private Language language;

    public Server setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public void save(DataCluster cluster) {
        cluster.getServerRepository().save(this);
    }
}