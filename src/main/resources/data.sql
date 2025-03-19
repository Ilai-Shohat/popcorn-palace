-- Insert test movies
INSERT INTO movies (title, genre, duration, rating, release_year) VALUES
    ('The Matrix', 'Action/Sci-Fi', 136, 8.7, 1999),
    ('Inception', 'Action/Sci-Fi', 148, 8.8, 2010),
    ('The Shawshank Redemption', 'Drama', 142, 9.3, 1994);

-- Insert test showtimes
INSERT INTO showtimes (movie_id, theater, start_time, end_time, price) VALUES
    (1, 'Theater 1', '2025-03-15 14:00:00+00', '2025-03-15 16:16:00+00', 12.50),
    (2, 'Theater 2', '2025-03-15 17:00:00+00', '2025-03-15 19:28:00+00', 15.00),
    (3, 'Theater 3', '2025-03-15 20:00:00+00', '2025-03-15 22:22:00+00', 13.50);

-- Tickets are commented out as they require existing showtimes and valid UUIDs
-- You can uncomment and modify if needed
-- INSERT INTO tickets (showtime_id, seat_number, user_id, booking_id) VALUES
--    (1, 'A1', '84438967-f68f-4fa0-b620-0f08217e76af'::uuid, 'd5f2cdd6-3a79-4d86-ab3c-deac2ae18910'::uuid),
--    (2, 'B2', '93c2eddc-67af-458c-a6b7-2342b21dc76a'::uuid, 'c3c85e8f-c6a3-4274-b636-40689336c6db'::uuid);