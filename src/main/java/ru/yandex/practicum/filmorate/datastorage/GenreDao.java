package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.datastorage.rowmappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GenreDao {
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL_GENRES = "SELECT * FROM GENRE_TABLE";

    private static final String SQL_GET_GENRE = "SELECT * FROM GENRE_TABLE WHERE ID = ? ";

    @Autowired
    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Map<String, Object>> listOfMapsOfGenres = jdbcTemplate.queryForList(SQL_GET_ALL_GENRES);

        List<Genre> listOfGenres = new ArrayList<>();

        for (Map<String, Object> map : listOfMapsOfGenres) {
            int id = (Integer) map.get("ID");
            String name = (String) map.get("NAME");
            Genre genre = new Genre(id, name);
            listOfGenres.add(genre);
        }
        listOfGenres = listOfGenres
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
        return new ResponseEntity<>(listOfGenres, HttpStatus.OK);
    }

    public ResponseEntity<Genre> getGenre(int id) {
        Genre genre = jdbcTemplate.queryForObject(SQL_GET_GENRE, new Object[]{id}, new GenreRowMapper());
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }
}
