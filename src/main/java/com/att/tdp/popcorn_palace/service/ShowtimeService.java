package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.InvalidTimeRangeException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieService movieService;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public boolean showtimeExists(Long id) {
        return showtimeRepository.existsById(id);
    }

    public Showtime getShowtimeById(Long id) {
        if (!showtimeExists(id)) {
            throw new ShowtimeNotFoundException("Showtime with ID " + id + " not found");
        }

        return showtimeRepository.findById(id).get();
    }

    public Showtime createShowtime(Showtime showtime) {
        validateTimeRange(showtime);
        validateMovieExists(showtime);
        validateNoOverlappingShowtimes(showtime, null);

        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, Showtime showtime) {
        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime with ID " + id + " not found"));

        validateMovieExists(showtime);
        BeanUtils.copyProperties(showtime, existingShowtime, "id");
        validateTimeRange(existingShowtime);
        validateNoOverlappingShowtimes(existingShowtime, id);

        return showtimeRepository.save(existingShowtime);
    }

    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime with ID " + id + " not found"));
                
        showtimeRepository.delete(showtime);
    }

    // Validates that the movie in the showtime exists in the database
    private void validateMovieExists(Showtime showtime) {
        if (showtime.getMovie() == null) {
            throw new MovieNotFoundException("Movie is required for showtime");
        }

        if (movieService.getMovieById(showtime.getMovie().getId()) == null) {
            throw new MovieNotFoundException("Movie with ID " + showtime.getMovie().getId() + " not found");
        }
    }

    // Validates that there are no overlapping showtimes for the same theater.
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

    // Validates that the start time is before the end time
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
