package bot.ryuu.snowball.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class Language {
    @Value("${language.power.path}")
    private String powerPath;

    @Value("${language.system.path}")
    private String systemPath;

    private static final HashMap<String, Message> messages = new HashMap<>();

    @Bean
    private void loadLanguagePower() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Wrap wrap = mapper.readValue(new File(powerPath), Wrap.class);

            for (Param param : wrap.params()) {
                messages.put(param.name(), new Message(param.en(), param.ru()));
            }

            System.out.println("[ -- FINISH LOAD POWER LANGUAGE -- ]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    private void loadLanguageSystem() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Wrap wrap = mapper.readValue(new File(systemPath), Wrap.class);

            for (Param param : wrap.params()) {
                messages.put(param.name(), new Message(param.en(), param.ru()));
            }

            System.out.println("[ -- FINISH LOAD SYSTEM LANGUAGE -- ]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String message(String code, String language) {
        if (messages.get(code) != null) {
            if (language.equals("en"))
                return messages.get(code).en();
            else
                return messages.get(code).ru();
        } else
            return "<empty>";
    }
}