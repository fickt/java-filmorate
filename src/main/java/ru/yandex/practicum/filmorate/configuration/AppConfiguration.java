package ru.yandex.practicum.filmorate.configuration;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.datastorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.datastorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;


/*
@ComponentScan
@Configuration
public class AppConfiguration {


    @Bean
    public FilmController filmControllerConfig() {
        return new FilmController(inMemoryFilmStorageConfig(), filmServiceConfig());
    }

    @Bean
    @Primary
    public UserController userControllerConfig() {
        return new UserController(userStorageConfig(), userServiceConfig());
    }

    @Bean
    @Primary
    public FilmStorage inMemoryFilmStorageConfig() {
        return new InMemoryFilmStorage();
    }

    @Bean
    public FilmService filmServiceConfig() {
        return new FilmService();
    }

    @Bean
    public UserService userServiceConfig() {
        return new UserService();
    }

    @Bean
    public UserStorage userStorageConfig() {
        return new InMemoryUserStorage();
    }

}
*/
