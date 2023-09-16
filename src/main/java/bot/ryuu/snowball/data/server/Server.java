package bot.ryuu.snowball.data.server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    private String id;

    private String language;

    public Server setLanguage(String language) {
        this.language = language;
        return this;
    }

    public void save(ServerRepository serverRepository) {
        serverRepository.save(this);
    }
}
