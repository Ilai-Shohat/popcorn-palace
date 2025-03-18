package com.att.tdp.popcorn_palace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    
    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public List<Showtime> getAllShowtimes() {
        return showtimeService.getAllShowtimes();
    }

    @PostMapping
    public String createShowtime(@RequestBody Showtime showtime) {
        showtimeService.createShowtime(showtime);
        return "Showtime created successfully";
    }

    @PutMapping("/update/{showtimeId}")
    public String updateShowtime(@PathVariable Long showtimeId, @RequestBody Showtime showtime) {
        showtimeService.updateShowtime(showtimeId, showtime);
        return "Showtime updated successfully";
    }

    @DeleteMapping("/{showtimeId}")
    public String deleteShowtime(@PathVariable Long showtimeId) {
        showtimeService.deleteShowtime(showtimeId);
        return "Showtime deleted successfully";
    }
}
