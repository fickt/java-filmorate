package ru.yandex.practicum.filmorate.datastorage.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre(
                resultSet.getInt("ID"),
                resultSet.getString("NAME")
        );
        return genre;
    }
}
