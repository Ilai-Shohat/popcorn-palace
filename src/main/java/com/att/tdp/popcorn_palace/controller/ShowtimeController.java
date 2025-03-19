package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.InvalidTimeRangeException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.mapper.ShowtimeMapper;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ShowtimeDTO> createShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeMapper.fromDTO(showtimeDTO);
        Showtime createdShowtime = showtimeService.createShowtime(showtime);
        return ResponseEntity.ok(showtimeMapper.toDTO(createdShowtime));
    }

    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long showtimeId,
            @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeMapper.fromDTO(showtimeDTO);
        Showtime updatedShowtime = showtimeService.updateShowtime(showtimeId, showtime);
        if (updatedShowtime != null) {
            return ResponseEntity.ok(showtimeMapper.toDTO(updatedShowtime));
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

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<String> handleMovieNotFoundException(MovieNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(OverlappingShowtimeException.class)
    public ResponseEntity<String> handleOverlappingShowtimeException(OverlappingShowtimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<String> handleInvalidTimeRangeException(InvalidTimeRangeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
