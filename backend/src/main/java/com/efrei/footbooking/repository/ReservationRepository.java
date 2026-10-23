package com.efrei.footbooking.repository;

import com.efrei.footbooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT r.heureDebut FROM Reservation r
            WHERE r.terrain.id = :terrainId AND r.dateReservation = :date
            """)
    List<LocalTime> findHeuresReservees(@Param("terrainId") Long terrainId,
                                        @Param("date") LocalDate date);
}