package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    @JsonFormat(pattern = "dd-MM-uuuu", shape = JsonFormat.Shape.STRING)
    private String birthday;
    private Set<User> listOfFriends = new HashSet<>();

    public void setBirthday(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        this.birthday = formatter.format(localDate);
    }

    public LocalDate getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(birthday, formatter);

    }
}
