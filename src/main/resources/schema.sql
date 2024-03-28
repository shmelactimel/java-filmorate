CREATE TABLE IF NOT EXISTS users (
                                     id bigint generated by default as identity primary key,
                                     email varchar(256) NOT NULL,
    login varchar(256) NOT NULL,
    name varchar(256),
    birthdate date
    );

CREATE TABLE IF NOT EXISTS friend (
                                      id bigint generated by default as identity primary key, -- Добавлен столбец id
                                      user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    friend_id bigint REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT friend_self CHECK (user_id <> friend_id),
    PRIMARY KEY (id), -- Изменен PRIMARY KEY
    UNIQUE (user_id, friend_id) -- Добавлено UNIQUE CONSTRAINT для предотвращения дублирования записей
    );

CREATE TABLE IF NOT EXISTS rating (
                                      id integer generated by default as identity primary key,
                                      name varchar(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS film (
                                    id bigint generated by default as identity primary key,
                                    name varchar(256) NOT NULL,
    description varchar(200),
    release_date date,
    duration integer,
    rating_id integer,
    CONSTRAINT film_duration_min CHECK (duration > 0),
    CONSTRAINT film_date_start CHECK (release_date > '1895-12-28'),
    CONSTRAINT fk_film_rating FOREIGN KEY(rating_id) REFERENCES rating(id)
    );

CREATE TABLE IF NOT EXISTS film_likes (
                                          film_id bigint REFERENCES film(id) ON DELETE CASCADE,
    user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
    );

CREATE TABLE IF NOT EXISTS genre (
                                     id integer generated by default as identity primary key,
                                     name varchar(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS film_genre (
                                          id bigint generated by default as identity primary key,
                                          film_id bigint,
                                          genre_id integer,
                                          CONSTRAINT fk_film_genre_film FOREIGN KEY(film_id) REFERENCES film(id) ON DELETE CASCADE,
    CONSTRAINT fk_film_genre_genre FOREIGN KEY(genre_id) REFERENCES genre(id)
    );
