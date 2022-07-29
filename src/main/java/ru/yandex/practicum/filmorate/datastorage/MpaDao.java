package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MpaDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<List<Mpa>> getAllMpa() {
        List<Map<String, Object>> listOfMapsOfMpa = jdbcTemplate.queryForList("SELECT * FROM RATING_TABLE");

        List<Mpa> listOfMpa = new ArrayList<>();

        for (Map<String, Object> map : listOfMapsOfMpa) {
            Mpa mpa = new Mpa();
            mpa.setId((Integer) map.get("RATING_ID_2"));
            mpa.setName((String) map.get("RATING_NAME"));

            listOfMpa.add(mpa);
        }
        listOfMpa = listOfMpa.stream().sorted(Comparator.comparing(Mpa::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(listOfMpa, HttpStatus.OK);

    }

    public ResponseEntity<Mpa> getMpa(int id) {
        Map<String, Object> mpaMap = jdbcTemplate.queryForMap("SELECT * FROM RATING_TABLE WHERE RATING_ID_2 = ? ", id);
        Mpa mpa = new Mpa();
        mpa.setName((String)mpaMap.get("RATING_NAME"));
        mpa.setId((Integer) mpaMap.get("RATING_ID_2"));

        return new ResponseEntity<>(mpa, HttpStatus.OK);
    }
}