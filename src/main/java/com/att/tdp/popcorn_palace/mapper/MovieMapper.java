package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.model.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO toDTO(Movie movie);

    Movie fromDTO(MovieDTO dto);
}
