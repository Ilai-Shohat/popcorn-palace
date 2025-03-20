package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    /**
     * Converts a Movie entity to a MovieDTO
     * 
     * @param movie The Movie entity to convert
     * @return The corresponding MovieDTO
     */
    public MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                movie.getRating(),
                movie.getReleaseYear());
    }

    /**
     * Converts a MovieDTO to a Movie entity
     * 
     * @param dto The MovieDTO to convert
     * @return The corresponding Movie entity
     */
    public Movie fromDTO(MovieDTO dto) {
        if (dto == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setId(dto.getId());
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDuration(dto.getDuration());
        movie.setRating(dto.getRating());
        movie.setReleaseYear(dto.getReleaseYear());

        return movie;
    }
}
