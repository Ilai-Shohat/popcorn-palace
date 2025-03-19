package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.InvalidTimeRangeException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieService movieService;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime createShowtime(Showtime showtime) {
        validateMovie(showtime);
        validateTimeRange(showtime);
        validateNoOverlappingShowtimes(showtime, null);
        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, Showtime showtime) {
        Optional<Showtime> existingShowtime = showtimeRepository.findById(id);
        if (existingShowtime.isPresent()) {
            validateMovie(showtime);

            Showtime updatedShowtime = existingShowtime.get();
            updatedShowtime.setMovie(showtime.getMovie());
            updatedShowtime.setPrice(showtime.getPrice());
            updatedShowtime.setTheater(showtime.getTheater());
            updatedShowtime.setStartTime(showtime.getStartTime());
            updatedShowtime.setEndTime(showtime.getEndTime());

            validateTimeRange(updatedShowtime);
            validateNoOverlappingShowtimes(updatedShowtime, id);

            return showtimeRepository.save(updatedShowtime);
        }
        return null;
    }

    public boolean deleteShowtime(Long id) {
        Optional<Showtime> showtime = showtimeRepository.findById(id);
        if (showtime.isPresent()) {
            showtimeRepository.delete(showtime.get());
            return true;
        }
        return false;
    }

    /**
     * Validates that the movie in the showtime exists in the database
     * 
     * @param showtime The showtime to validate
     * @throws MovieNotFoundException if the movie doesn't exist
     */
    private void validateMovie(Showtime showtime) {
        if (showtime.getMovie() == null) {
            throw new MovieNotFoundException("Movie is required for showtime");
        }

        if (movieService.getMovieById(showtime.getMovie().getId()) == null) {
            throw new MovieNotFoundException("Movie with ID " + showtime.getMovie().getId() + " not found");
        }
    }

    /**
     * Validates that there are no overlapping showtimes for the same theater.
     * 
     * @param showtime   The showtime to validate
     * @param showtimeId The ID of the showtime being updated (null for new
     *                   showtimes)
     * @throws OverlappingShowtimeException if there are overlapping showtimes
     */
    private void validateNoOverlappingShowtimes(Showtime showtime, Long showtimeId) {
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtimeId);

        if (!overlappingShowtimes.isEmpty()) {
            Showtime conflictingShowtime = overlappingShowtimes.get(0);
            throw new OverlappingShowtimeException(
                    String.format("Showtime overlaps with existing showtime in theater %s from %s to %s",
                            conflictingShowtime.getTheater(),
                            conflictingShowtime.getStartTime(),
                            conflictingShowtime.getEndTime()));
        }
    }

    /**
     * Validates that the start time is before the end time
     * 
     * @param showtime The showtime to validate
     * @throws InvalidTimeRangeException if end time is not after start time
     */
    private void validateTimeRange(Showtime showtime) {
        if (showtime.getStartTime() == null) {
            throw new InvalidTimeRangeException("Start time cannot be null");
        }

        if (showtime.getEndTime() == null) {
            throw new InvalidTimeRangeException("End time cannot be null");
        }

        if (!showtime.getStartTime().isBefore(showtime.getEndTime())) {
            throw new InvalidTimeRangeException("End time must be after start time");
        }
    }
}
