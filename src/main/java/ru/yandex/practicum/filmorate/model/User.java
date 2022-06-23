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
    @JsonFormat(pattern = "uuuu-MM-dd", shape = JsonFormat.Shape.STRING) //"dd-MM-uuuu"
    private String birthday;
    private Set<User> listOfFriends = new HashSet<>();

    public void setBirthday(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        this.birthday = formatter.format(localDate);
    }

    public LocalDate getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(birthday, formatter);

    }
}
