package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    @JsonFormat(pattern = "uuuu-MM-dd", shape = JsonFormat.Shape.STRING)
    private String birthday;
    private Set<Long> listOfFriends = new HashSet<>();

     public void setBirthdayAsString (String date) {
         birthday = date;
     }

    public void setBirthday(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        this.birthday = formatter.format(localDate);
    }

    public LocalDate getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(birthday, formatter);

    }

    public String getBirthdayAsString() {
         return birthday;
    }

}
