package ru.yandex.practicum.filmorate.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@NoArgsConstructor
@ConfigurationProperties("filmorate")
public class AppConfig {
    private FilmSetting film;

    @Setter
    @Getter
    public static class FilmSetting {
        private FilmPopularSetting popular;
    }

    @Setter
    @Getter
    public static class FilmPopularSetting {
        private FilmPopularShowSetting show;
    }

    @Setter
    @Getter
    public static class FilmPopularShowSetting {
        private int count;
    }
}
