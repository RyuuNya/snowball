package bot.ryuu.snowball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnowballApplication {
	private static String TOKEN;

	public static void main(String[] args) {
		if (args.length > 0)
			TOKEN = args[0];

		SpringApplication.run(SnowballApplication.class, args);
	}

	public static String getToken() {
		return TOKEN;
	}
}