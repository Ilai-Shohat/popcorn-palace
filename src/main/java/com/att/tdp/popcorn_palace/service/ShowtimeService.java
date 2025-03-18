package com.att.tdp.popcorn_palace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public void createShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    public void updateShowtime(Long showtimeId, Showtime showtime) {
        Showtime existingShowtime = showtimeRepository.getReferenceById(showtimeId);
        // existingShowtime.setShowtimeId(showtime.getShowtimeId());
        // existingShowtime.setShowtimeDate(showtime.getShowtimeDate());
        // existingShowtime.setShowtimeTime(showtime.getShowtimeTime());
        // existingShowtime.setShowtimeMovieTitle(showtime.getShowtimeMovieTitle());
        // existingShowtime.setShowtimeTheaterName(showtime.getShowtimeTheaterName());
        // existingShowtime.setShowtimeTheaterLocation(showtime.getShowtimeTheaterLocation());
        showtimeRepository.save(existingShowtime);
    }

    public void deleteShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.getReferenceById(showtimeId);
        showtimeRepository.delete(showtime);
    }
}
