CREATE TABLE IF NOT EXISTS genre (
    genre_id int PRIMARY KEY auto_increment,
    name varchar(50)
    );

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id int PRIMARY KEY auto_increment,
    name varchar(50)
    );

CREATE TABLE IF NOT EXISTS movies (
    film_id bigint PRIMARY KEY auto_increment,
    name varchar(200),
    description varchar,
    release_date date,
    duration int,
    movie_mpa_id int,
    FOREIGN KEY (movie_mpa_id)
        REFERENCES mpa(mpa_id)
    );

CREATE TABLE IF NOT EXISTS movie_genre (
    film_id bigint,
    genre_id bigint,
    FOREIGN KEY (film_id)
        REFERENCES movies(film_id),
    FOREIGN KEY (genre_id)
        REFERENCES genre(genre_id)
    );

CREATE TABLE IF NOT EXISTS users (
    user_id bigint PRIMARY KEY auto_increment,
    email varchar(200),
    login varchar(200),
    name varchar(200),
    birthday date
    );

CREATE TABLE IF NOT EXISTS likes (
    film_id bigint,
    user_id bigint,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id)
        REFERENCES movies(film_id),
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS approved_friends (
    approve_id bigint PRIMARY KEY auto_increment,
    approved bool
);

CREATE TABLE IF NOT EXISTS friends (
    user_id bigint,
    friends_id bigint,
    approve_id bigint,
    FOREIGN KEY (user_id)
        REFERENCES users(user_id),
    FOREIGN KEY (approve_id)
        REFERENCES approved_friends(approve_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id,friends_id),
    UNIQUE (user_id,friends_id),
    CHECK (user_id <> friends_id)
    );