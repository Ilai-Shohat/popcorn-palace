-- Insert test movies
INSERT INTO movies (title, genre, duration, rating, release_year) VALUES
    ('The Matrix', 'Action/Sci-Fi', 136, 8.7, 1999),
    ('Inception', 'Action/Sci-Fi', 148, 8.8, 2010),
    ('The Shawshank Redemption', 'Drama', 142, 9.3, 1994),
    ('The Dark Knight', 'Action/Drama', 152, 9.0, 2008),
    ('Pulp Fiction', 'Crime/Drama', 154, 8.9, 1994),
    ('Fight Club', 'Drama/Thriller', 139, 8.8, 1999),
    ('Forrest Gump', 'Drama/Romance', 142, 8.8, 1994),
    ('The Godfather', 'Crime/Drama', 175, 9.2, 1972),
    ('Interstellar', 'Adventure/Sci-Fi', 169, 8.6, 2014),
    ('The Lord of the Rings: The Return of the King', 'Adventure/Fantasy', 201, 8.9, 2003);

-- Insert test showtimes
INSERT INTO showtimes (movie_id, theater, start_time, end_time, price) VALUES
    (1, 'Theater 1', '2025-03-15 14:00:00+00', '2025-03-15 16:16:00+00', 12.50),
    (2, 'Theater 2', '2025-03-15 17:00:00+00', '2025-03-15 19:28:00+00', 15.00),
    (3, 'Theater 3', '2025-03-15 20:00:00+00', '2025-03-15 22:22:00+00', 13.50),
    (4, 'Theater 1', '2025-03-15 19:00:00+00', '2025-03-15 21:32:00+00', 14.00),
    (5, 'Theater 2', '2025-03-15 21:00:00+00', '2025-03-15 23:34:00+00', 14.50),
    (6, 'Theater 3', '2025-03-16 14:00:00+00', '2025-03-16 16:19:00+00', 12.00),
    (7, 'Theater 4', '2025-03-16 15:00:00+00', '2025-03-16 17:22:00+00', 13.00),
    (8, 'Theater 1', '2025-03-16 17:00:00+00', '2025-03-16 19:55:00+00', 15.50),
    (9, 'Theater 2', '2025-03-16 19:00:00+00', '2025-03-16 21:49:00+00', 16.00),
    (10, 'Theater 3', '2025-03-16 20:00:00+00', '2025-03-16 23:21:00+00', 16.50);