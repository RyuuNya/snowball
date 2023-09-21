package bot.ryuu.snowball.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LanguageV2 {
    private static String path;

    @Value("${languageV2.system.path}")
    public void setPath(String path) {
        LanguageV2.path = path;
    }

    @Nullable
    public static String message(String name, Type type) {
        String message = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            Wrap wrap = mapper.readValue(new File(path), Wrap.class);

            for (Param param : wrap.params()) {
                if (param.name().equals(name)) {
                    message = (type == Type.EN) ? param.en() : param.ru();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
