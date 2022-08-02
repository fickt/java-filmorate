package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.datastorage.rowmappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MpaDao {

    private static final String SQL_GET_MPA = "SELECT * FROM RATING_TABLE WHERE RATING_ID_2 = ? ";
    private static final String SQL_GET_ALL_MPA = "SELECT * FROM RATING_TABLE";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<List<Mpa>> getAllMpa() {
        List<Map<String, Object>> listOfMapsOfMpa = jdbcTemplate.queryForList(SQL_GET_ALL_MPA);

        List<Mpa> listOfMpa = new ArrayList<>();

        for (Map<String, Object> map : listOfMapsOfMpa) {
            Mpa mpa = new Mpa();
            mpa.setId((Integer) map.get("RATING_ID_2"));
            mpa.setName((String) map.get("RATING_NAME"));

            listOfMpa.add(mpa);
        }
        listOfMpa = listOfMpa
                .stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
        return new ResponseEntity<>(listOfMpa, HttpStatus.OK);

    }

    public ResponseEntity<Mpa> getMpa(int id) {
        Mpa mpa = jdbcTemplate.queryForObject(SQL_GET_MPA, new Object[]{id}, new MpaRowMapper());
        return new ResponseEntity<>(mpa, HttpStatus.OK);
    }
}