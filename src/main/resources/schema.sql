DROP TABLE IF EXISTS FILM_TABLE CASCADE;
DROP TABLE IF EXISTS USER_TABLE CASCADE;
DROP TABLE IF EXISTS FRIENDS_TABLE CASCADE;
DROP TABLE IF EXISTS LIKE_USER_TABLE CASCADE;
DROP TABLE IF EXISTS GENRE_FILM_TABLE CASCADE;

CREATE TABLE IF NOT EXISTS GENRE_TABLE
(
    GENRE_ID   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    GENRE_NAME VARCHAR(100),
    CONSTRAINT unique_genres UNIQUE (GENRE_NAME)
);


CREATE TABLE IF NOT EXISTS RATING_TABLE
(
    RATING_ID_2 INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    RATING_NAME VARCHAR(100),
    CONSTRAINT unique_ratings UNIQUE (RATING_NAME)
);

CREATE TABLE IF NOT EXISTS FILM_TABLE
(
    ID           LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME         VARCHAR(100),
    DESCRIPTION  TEXT,
    RELEASE_DATE VARCHAR(100),
    GENRE_ID     INT,
    RATING_ID    INT,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE_TABLE (ID) ON DELETE RESTRICT,
    FOREIGN KEY (RATING_ID) REFERENCES RATING_TABLE (RATING_ID_2) ON DELETE RESTRICT,
    DURATION     LONG,
    RATE         INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS USER_TABLE
(
    ID       LONG GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME     VARCHAR(100),
    LOGIN    VARCHAR(100),
    EMAIL    VARCHAR(100),
    BIRTHDAY VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS LIKE_USER_TABLE
(
    FILM_ID LONG,
    USER_ID LONG,
    FOREIGN KEY (FILM_ID) REFERENCES FILM_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USER_TABLE (ID) ON DELETE CASCADE,
    CONSTRAINT user_like_controller UNIQUE (FILM_ID, USER_ID)
);


CREATE TABLE IF NOT EXISTS FRIENDS_TABLE
(
    USER_ID              LONG,
    ANOTHER_USER_ID      LONG,
    USER_REQUEST         BOOLEAN,
    ANOTHER_USER_REQUEST BOOLEAN,
    STATUS_FRIENDS       BOOLEAN,
    CONSTRAINT user_friends_controller UNIQUE (USER_ID, ANOTHER_USER_ID)
);

CREATE TABLE IF NOT EXISTS GENRE_FILM_TABLE
(
    FILM_ID  INT,
    GENRE_ID INT,
    FOREIGN KEY (FILM_ID) REFERENCES FILM_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE_TABLE (ID) ON DELETE CASCADE,
    CONSTRAINT unique_film_genres UNIQUE (FILM_ID, GENRE_ID)
);







