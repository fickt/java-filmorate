package ru.yandex.practicum.filmorate.datastorage.rowmappers;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setLogin(resultSet.getString("login"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthdayAsString(resultSet.getString("birthday"));
        return user;
    }

    public static List<User> userMapper(List<Map<String, Object>> listOfMaps) {
        List<User> list = new ArrayList<>();

        for (Map<String, Object> map : listOfMaps) {
            User user = new User();
            user.setId((Long) map.get("id"));
            user.setName((String) map.get("name"));
            user.setLogin((String) map.get("login"));
            user.setEmail((String) map.get("email"));
            user.setBirthdayAsString((String) map.get("birthday"));
            list.add(user);
        }
        return list;
    }
}