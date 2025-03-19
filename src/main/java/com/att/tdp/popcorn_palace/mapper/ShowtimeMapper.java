package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeMapper {

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
                showtime.getPrice(),
                showtime.getMovie().getId(),
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime());
    }
}
