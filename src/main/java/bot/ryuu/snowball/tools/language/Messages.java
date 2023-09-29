package bot.ryuu.snowball.tools.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Messages {
    private static String path;

    @Value("${language.path}")
    public void setPath(String path) {
        Messages.path = path;
    }

    @Nullable
    public static String message(String name, Language type) {
        String message = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            Wrap wrap = mapper.readValue(new File(path), Wrap.class);

            for (Param param : wrap.params()) {
                if (param.name().equals(name)) {
                    message = (type == Language.EN) ? param.en() : param.ru();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
