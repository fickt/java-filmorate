package ru.yandex.practicum.filmorate.datastorage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.datastorage.rowmappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final String SQL_ADD_FILM = "INSERT INTO film_table" +
            "(NAME, DESCRIPTION, RELEASE_DATE, RATING_ID, DURATION, RATE) VALUES (?,?,?,?,?,?)";

    private static final String SQL_GET_FILM = "SELECT * FROM FILM_TABLE LEFT JOIN RATING_TABLE " +
            "ON FILM_TABLE.RATING_ID = RATING_TABLE.RATING_ID_2 WHERE FILM_TABLE.ID=?";

    private static final String SQL_ADD_GENRES_OF_FILM = "INSERT INTO GENRE_FILM_TABLE(FILM_ID, GENRE_ID) VALUES (?,?) ";

    private static final String SQL_CHECK_FILM_IF_EXISTS = "SELECT * FROM FILM_TABLE WHERE id=? ";

    private static final String SQL_GET_ALL_FILMS = "SELECT * FROM film_table LEFT JOIN RATING_TABLE " +
            "ON FILM_TABLE.RATING_ID = RATING_TABLE.RATING_ID_2";

    private static final String SQL_GET_GENRES_OF_FILM = "SELECT * FROM GENRE_FILM_TABLE LEFT JOIN " +
            "GENRE_TABLE GT ON GENRE_FILM_TABLE.GENRE_ID = GT.ID WHERE GENRE_FILM_TABLE.FILM_ID=? ";

    private static final String SQL_UPDATE_FILM = "UPDATE FILM_TABLE SET NAME=?, DESCRIPTION=?," +
            "DURATION=?, RELEASE_DATE=?, RATING_ID=? WHERE ID=?";

    private static final String SQL_DELETE_GENRES_OF_FILM = "DELETE  FROM GENRE_FILM_TABLE WHERE FILM_ID=? ";

    private static final String SQL_PUT_LIKE = "INSERT INTO LIKE_USER_TABLE (FILM_ID, USER_ID)" +
            "VALUES (?,?)";

    private static final String SQL_UPDATE_RATE_OF_FILM = "UPDATE FILM_TABLE SET RATE=?";

    private static final String SQL_GET_RATE_OF_FILM = "SELECT COUNT(FILM_ID) " +
            "FROM LIKE_USER_TABLE WHERE FILM_ID=";

    private static final String SQL_DELETE_LIKE = "DELETE FROM LIKE_USER_TABLE WHERE FILM_ID=? AND " +
            "USER_ID=?";

    private static final String SQL_GET_ALL_FILMS_ORDERED_BY_RATE = "SELECT * FROM film_table LEFT JOIN RATING_TABLE " +
            "ON FILM_TABLE.RATING_ID = RATING_TABLE.RATING_ID_2 ORDER BY RATE DESC";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_ADD_FILM, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDateAsString());
            stmt.setInt(4, film.getMpa().getId());
            stmt.setLong(5, film.getDuration());
            stmt.setLong(6, film.getRate());
            return stmt;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();

        if(film.getGenres() != null) {
            for (Genre genre : film.getGenres())
                jdbcTemplate.update(SQL_ADD_GENRES_OF_FILM, filmId, genre.getId());
        }
        return getFilm(filmId);
    }

    @Override
    public boolean containsFilm(long filmId) {
        try {
            jdbcTemplate.queryForObject(SQL_CHECK_FILM_IF_EXISTS, new Object[]{filmId},
                    new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Film> getAllFilms() {
     List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_ALL_FILMS);

     List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            System.out.println(map.keySet());
            System.out.println(map.values());
            Film film = new Film();
            film.setId((Long) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateAsString((String)map.get("RELEASE_DATE"));
            if(map.containsKey("RATING_NAME")) {
                film.getMpa().setName((String) map.get("RATING_NAME"));
            }
            if(map.containsKey("RATING_ID_2")) {
                film.getMpa().setId((Integer) map.get("RATING_ID_2"));
            }
            film.setDurationAsLong((Long)map.get("DURATION"));
            film.setRate((Integer)map.get("RATE"));

              List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, map.get("ID"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                System.out.println(genreMap.keySet());
                System.out.println(genreMap.values());

                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }

            listOfFilms.add(film);
     }
        return listOfFilms;
    }

    @Override
    public Film getFilm(long id) {

        List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_FILM, id);

        List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, id);

        return mapsToList(films, genresOfFilm).get(0);

    }

    @Override
    public void updateFilm(Film film) {

        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        jdbcTemplate.update(SQL_DELETE_GENRES_OF_FILM, film.getId());
        for (Genre genre : film.getGenres()) {
            try {
                jdbcTemplate.update(SQL_ADD_GENRES_OF_FILM, film.getId(), genre.getId());
            } catch (DataIntegrityViolationException e) {
                continue;
            }
        }
    }

    private List<Film> mapsToList (List<Map<String, Object>> films, List<Map<String, Object>> genresOfFilm) {    /*convert maps of films and genres
                                                                                                                 to list of films*/
        List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            System.out.println(map.keySet());
            System.out.println(map.values());
            Film film = new Film();
            film.setId((Long) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateAsString((String) map.get("RELEASE_DATE"));
            if (map.containsKey("RATING_NAME")) {
                film.getMpa().setName((String) map.get("RATING_NAME"));
            }
            if (map.containsKey("RATING_ID_2")) {
                film.getMpa().setId((Integer) map.get("RATING_ID_2"));
            }
            film.setDurationAsLong((Long) map.get("DURATION"));
            film.setRate((Integer) map.get("RATE"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                System.out.println(genreMap.keySet());
                System.out.println(genreMap.values());

                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }
            listOfFilms.add(film);
        }
        return listOfFilms;

    }

    @Override
    public void putLike(long filmId, long userId) {
        jdbcTemplate.update(SQL_PUT_LIKE, filmId, userId);
        int rate = jdbcTemplate.queryForObject(SQL_GET_RATE_OF_FILM + filmId, Integer.class);
        jdbcTemplate.update(SQL_UPDATE_RATE_OF_FILM, rate);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE, filmId, userId);
        int rate = jdbcTemplate.queryForObject(SQL_GET_RATE_OF_FILM + filmId, Integer.class);
        jdbcTemplate.update(SQL_UPDATE_RATE_OF_FILM, rate);

    }

    public List<Film> getTopFilms(Integer count) {
        List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_ALL_FILMS_ORDERED_BY_RATE);

        List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            System.out.println(map.keySet());
            System.out.println(map.values());
            Film film = new Film();
            film.setId((Long) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateAsString((String)map.get("RELEASE_DATE"));
            if(map.containsKey("RATING_NAME")) {
                film.getMpa().setName((String) map.get("RATING_NAME"));
            }
            if(map.containsKey("RATING_ID_2")) {
                film.getMpa().setId((Integer) map.get("RATING_ID_2"));
            }
            film.setDurationAsLong((Long)map.get("DURATION"));
            film.setRate((Integer)map.get("RATE"));

            List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, map.get("ID"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                System.out.println(genreMap.keySet());
                System.out.println(genreMap.values());

                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }

            listOfFilms.add(film);
        }

        Collections.reverse(listOfFilms);
        return listOfFilms
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}