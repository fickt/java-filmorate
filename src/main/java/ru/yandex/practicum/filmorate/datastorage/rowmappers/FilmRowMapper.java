package ru.yandex.practicum.filmorate.datastorage.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
   public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        Film film = new Film();

        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDateAsString(resultSet.getString("release_date"));
        film.setDurationAsLong(resultSet.getLong("duration"));
        film.setRate(resultSet.getInt("rate"));
        return film;
    }
}
