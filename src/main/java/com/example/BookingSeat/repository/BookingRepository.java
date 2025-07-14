package com.example.BookingSeat.repository;

import com.example.BookingSeat.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;



import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingRepository extends JpaRepository<Booking, String> {
    boolean existsByDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndBookTypeAndSubType(
            LocalDate date, LocalTime endTime, LocalTime startTime, Booking.BookType bookType, String subType
    );
}
