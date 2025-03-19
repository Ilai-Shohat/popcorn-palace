package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    /**
     * Find showtimes that overlap with the given time range in the same theater.
     * A showtime overlaps if it:
     * - Is in the same theater
     * - Has a start time that falls within the new showtime
     * - Has an end time that falls within the new showtime
     * - Completely encompasses the new showtime
     */
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND " +
            "((s.startTime >= :startTime AND s.startTime < :endTime) OR " +
            "(s.endTime > :startTime AND s.endTime <= :endTime) OR " +
            "(s.startTime <= :startTime AND s.endTime >= :endTime)) " +
            "AND (s.id <> :showtimeId OR :showtimeId IS NULL)")
    List<Showtime> findOverlappingShowtimes(
            @Param("theater") String theater,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("showtimeId") Long showtimeId);
}