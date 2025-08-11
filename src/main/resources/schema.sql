  CREATE TABLE IF NOT EXISTS mpa (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(5)
  );

  CREATE TABLE IF NOT EXISTS genres (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(20)
  );

  CREATE TABLE IF NOT EXISTS films (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER,
    FOREIGN KEY (mpa_id) REFERENCES mpa(id) ON DELETE SET NULL
  );

  CREATE TABLE IF NOT EXISTS films_genre (
    id IDENTITY NOT NULL PRIMARY KEY,
    film_id BIGINT,
    genre_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS users (
    id IDENTITY NOT NULL PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(30) NOT NULL,
    name VARCHAR(40),
    birthday DATE NOT NULL
  );

  CREATE TABLE IF NOT EXISTS films_liked (
    id IDENTITY NOT NULL PRIMARY KEY,
    film_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS users_friends (
    id IDENTITY NOT NULL PRIMARY KEY,
    user_id BIGINT,
    friend_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS reviews (
    id IDENTITY NOT NULL PRIMARY KEY,
    film_id BIGINT,
    user_id BIGINT,
    content VARCHAR,
    positiv BOOLEAN,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_reviews UNIQUE(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS reviews_likes (
    id IDENTITY NOT NULL PRIMARY KEY,
    review_id BIGINT,
    user_id BIGINT,
    like_value INTEGER,
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_reviews_likes UNIQUE(review_id, user_id)
);

CREATE TABLE IF NOT EXISTS directors (
    id IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS films_director (
    film_id BIGINT,
    director_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES directors(id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_films_director UNIQUE(film_id, director_id)
);