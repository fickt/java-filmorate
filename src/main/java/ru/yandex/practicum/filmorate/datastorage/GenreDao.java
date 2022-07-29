package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GenreDao {
   private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Map<String, Object>> listOfMapsOfGenres = jdbcTemplate.queryForList("SELECT * FROM GENRE_TABLE");

        List<Genre> listOfGenres = new ArrayList<>();

        for (Map<String, Object> map : listOfMapsOfGenres) {
            int id = (Integer) map.get("ID");
            String name = (String) map.get("NAME");
            Genre genre = new Genre(id, name);
            listOfGenres.add(genre);
        }
       listOfGenres = listOfGenres.stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(listOfGenres, HttpStatus.OK);
    }

    public ResponseEntity<Genre> getGenre(int id) {

        Map<String, Object> map = jdbcTemplate.queryForMap("SELECT * FROM GENRE_TABLE WHERE ID = ? ", id);


        return new ResponseEntity<>(new Genre((Integer)map.get("ID"), (String)map.get("NAME")), HttpStatus.OK);
    }
}
