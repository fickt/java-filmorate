package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


@Data
public class Film {
    private long id;
    private String name;
    private String description;
    @JsonFormat(pattern = "uuuu-MM-dd", shape = JsonFormat.Shape.STRING)
    private String releaseDate;
    @JsonFormat(pattern = "MINUTES", shape = JsonFormat.Shape.NUMBER)
    private long duration;
    private Set<Long> personsLikedFilm = new HashSet<>();
    private Set<Genre> genres = new TreeSet<>();
    private Mpa mpa = new Mpa();
    private int rate = 0;


    public void setReleaseDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        this.releaseDate = formatter.format(localDate);
    }

    public void setReleaseDateAsString(String localDate) { //only for DATABASE
        this.releaseDate = localDate;
    }

    public void setDurationAsLong(long duration) {   //only for DATABASE
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(releaseDate, formatter);

    }

    public void setDuration(Duration duration) {   // helps to cope with serialization problems
        this.duration = duration.toMinutes();
    }


    public long getDuration() {  // helps to cope with serialization problems
        return this.duration;
    }

    public Duration getDurationForComparing() {  // helps to cope with serialization problems
        Duration duration1 = Duration.of(duration, ChronoUnit.MINUTES); //
        return duration1;
    }

    public void updateAmountOfLikes() {
        rate = personsLikedFilm.size();
    }

    public String getReleaseDateAsString() {
        return releaseDate;
    }
}
