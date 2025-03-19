package com.att.tdp.popcorn_palace.service;

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

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, Showtime showtime) {
        Optional<Showtime> existingShowtime = showtimeRepository.findById(id);
        if (existingShowtime.isPresent()) {
            Showtime updatedShowtime = existingShowtime.get();
            updatedShowtime.setPrice(showtime.getPrice());
            updatedShowtime.setTheater(showtime.getTheater());
            updatedShowtime.setStartTime(showtime.getStartTime());
            updatedShowtime.setEndTime(showtime.getEndTime());
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
}
