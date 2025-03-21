package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.mapper.ShowtimeMapper;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private ShowtimeMapper showtimeMapper;

    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long showtimeId) {
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);

        return ResponseEntity.ok(showtimeMapper.toDTO(showtime));
    }

    @PostMapping
    public ResponseEntity<ShowtimeDTO> createShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeMapper.fromDTO(showtimeDTO);
        Showtime createdShowtime = showtimeService.createShowtime(showtime);

        return ResponseEntity.ok(showtimeMapper.toDTO(createdShowtime));
    }

    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> updateShowtime(@PathVariable Long showtimeId,
            @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeMapper.fromDTO(showtimeDTO);
        showtimeService.updateShowtime(showtimeId, showtime);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
        showtimeService.deleteShowtime(showtimeId);

        return ResponseEntity.ok().build();
    }
}
