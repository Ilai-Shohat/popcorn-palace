package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeMapper {

    @Autowired
    private MovieService movieService;

    /**
     * Converts a Showtime entity to a ShowtimeDTO
     * 
     * @param showtime The Showtime entity to convert
     * @return The corresponding ShowtimeDTO
     */
    public ShowtimeDTO toDTO(Showtime showtime) {
        if (showtime == null) {
            return null;
        }

        return new ShowtimeDTO(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getPrice(),
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime());
    }

    /**
     * Converts a ShowtimeDTO to a Showtime entity
     * 
     * @param dto The ShowtimeDTO to convert
     * @return The corresponding Showtime entity
     */
    public Showtime fromDTO(ShowtimeDTO dto) {
        if (dto == null) {
            return null;
        }

        Movie movie = movieService.getMovieById(dto.getMovieId());
        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID " + dto.getMovieId() + " not found");
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setPrice(dto.getPrice());
        showtime.setTheater(dto.getTheater());
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(dto.getEndTime());

        return showtime;
    }
}