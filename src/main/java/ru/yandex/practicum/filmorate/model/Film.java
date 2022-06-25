package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;


@Data
@Component
public class Film {
    private long id;
    private String name;
    private String description;
    @JsonFormat(pattern = "uuuu-MM-dd", shape = JsonFormat.Shape.STRING)
    private String releaseDate;
    //private long duration;
    @JsonFormat(pattern = "MINUTES", shape = JsonFormat.Shape.NUMBER)
    private long duration;
    private Set<User> personsLikedFilm = new HashSet<>();
    private int rate = 0;


    public void setReleaseDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        this.releaseDate = formatter.format(localDate);
    }

    public LocalDate getReleaseDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(releaseDate, formatter);

    }

    public void setDuration(Duration duration) {   // helps to cope with serialization problems
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm");
       this.duration = duration.toMinutes();
      //  this.duration =  duration.toSecondsPart() * 19;
    }


        public long getDuration() {  // helps to cope with serialization problems
        return this.duration;
    }

    public Duration getDurationForComparing() {  // helps to cope with serialization problems
        Duration duration1 = Duration.of(duration, ChronoUnit.MINUTES); // from int to Duration
        return duration1;
    }

    public void updateAmountOfLikes() {
        rate = personsLikedFilm.size();
    }
}
