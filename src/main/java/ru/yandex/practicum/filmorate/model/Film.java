package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;


@Data
public class Film {
    private int id;
    private String name;
    private String description;
    @JsonFormat(pattern = "dd-MM-uuuu", shape = JsonFormat.Shape.STRING)
    private String releaseDate;
    private Long durationInLong;
    private transient Duration duration;

    public void setReleaseDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        this.releaseDate = formatter.format(localDate);
    }

    public LocalDate getReleaseDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(releaseDate, formatter);

    }

    public void setDuration(Duration duration) {   // helps to cope with serialization problems
        this.duration = duration;
        this.durationInLong = duration.toSeconds();
    }

    public Duration getDuration() {  // helps to cope with serialization problems
        duration = Duration.of(durationInLong, ChronoUnit.SECONDS);
        return duration;
    }
}
