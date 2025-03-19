CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL UNIQUE,
    genre VARCHAR(64) NOT NULL,
    duration INT NOT NULL,
    rating DECIMAL(3, 2) NOT NULL,
    release_year INT NOT NULL
);

CREATE TABLE IF NOT EXISTS showtimes (
    id SERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater VARCHAR(64) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    -- Adding a foreign key constraint to ensure that the movie_id references an existing movie
    FOREIGN KEY (movie_id) REFERENCES movies(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tickets (
    id SERIAL PRIMARY KEY,
    showtime_id BIGINT NOT NULL,
    seat_number VARCHAR(64) NOT NULL,
    user_id UUID NOT NULL,
    booking_id UUID NOT NULL,
    UNIQUE(showtime_id, seat_number)  -- Adding a unique constraint to prevent double booking,
    -- Adding a foreign key constraint to ensure that the showtime_id references an existing showtime
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
    ON DELETE CASCADE
);