package ru.yandex.practicum.filmorate.datastorage.rowmappers;

import ru.yandex.practicum.filmorate.model.Mpa;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet resultSet, int i) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("RATING_ID_2"));
        mpa.setName(resultSet.getString("RATING_NAME"));

        return mpa;
    }
}
