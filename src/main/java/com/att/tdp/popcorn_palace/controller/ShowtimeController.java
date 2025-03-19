package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.mapper.ShowtimeMapper;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private ShowtimeMapper showtimeMapper;

    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> getShowtimeById(@PathVariable Long showtimeId) {
        Optional<Showtime> showtimeOpt = showtimeService.getShowtimeById(showtimeId);
        return showtimeOpt
                .map(showtime -> ResponseEntity.ok(showtimeMapper.toDTO(showtime)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShowtimeDTO> createShowtime(@RequestBody Showtime showtime) {
        Showtime createdShowtime = showtimeService.createShowtime(showtime);
        return ResponseEntity.ok(showtimeMapper.toDTO(createdShowtime));
    }

    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long showtimeId, @RequestBody Showtime showtime) {
        Showtime updatedShowtime = showtimeService.updateShowtime(showtimeId, showtime);
        if (updatedShowtime != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<?> deleteShowtime(@PathVariable Long showtimeId) {
        boolean deleted = showtimeService.deleteShowtime(showtimeId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
