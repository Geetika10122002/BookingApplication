package com.example.BookingSeat.controller;

import com.example.BookingSeat.dto.BookingRequest;

import com.example.BookingSeat.entity.Booking;

import com.example.BookingSeat.entity.Booking.BookType;

import com.example.BookingSeat.entity.Booking.RecurrenceType;

import com.example.BookingSeat.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.time.LocalTime;

import java.util.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            List<LocalDate> bookingDates = new ArrayList<>();
            LocalDate startDate = LocalDate.parse(request.date);
            if (request.recurrence != null &&
                    (request.recurrence.type == RecurrenceType.DAILY || request.recurrence.type == RecurrenceType.WEEKLY)) {

                if (request.recurrence.endDate == null) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "error", "MISSING_END_DATE",
                            "message", "endDate is required for recurrence type " + request.recurrence.type
                    ));
                }

                LocalDate end = LocalDate.parse(request.recurrence.endDate);
                if (end.isBefore(startDate)) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "error", "INVALID_DATE_RANGE",
                            "message", "endDate must be after start date"
                    ));
                }
            }
            // Recurrence handling
            if (request.recurrence == null || request.recurrence.type == RecurrenceType.NONE) {
                bookingDates.add(startDate);
            } else {
                switch (request.recurrence.type) {
                    case DAILY -> {
                        LocalDate end = LocalDate.parse(request.recurrence.endDate);
                        for (LocalDate d = startDate; !d.isAfter(end); d = d.plusDays(1)) {
                            bookingDates.add(d);
                        }
                    }
                    case WEEKLY -> {
                        LocalDate end = LocalDate.parse(request.recurrence.endDate);
                        for (LocalDate d = startDate; !d.isAfter(end); d = d.plusWeeks(1)) {
                            bookingDates.add(d);
                        }
                    }
                    case CUSTOM -> {
                        if (request.recurrence.customDates == null || request.recurrence.customDates.isEmpty()) {
                            return ResponseEntity.badRequest().body(Map.of(
                                    "success", false,
                                    "error", "INVALID_RECURRENCE",
                                    "message", "Custom dates required for recurrence type CUSTOM"
                            ));
                        }
                        for (String dateStr : request.recurrence.customDates) {
                            bookingDates.add(LocalDate.parse(dateStr));
                        }
                    }
                }
            }

            // Parse time
            LocalTime startTime = LocalTime.parse(request.startTime);
            LocalTime endTime = LocalTime.parse(request.endTime);

            // Validation
            if (endTime.isBefore(startTime)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "error", "INVALID_TIME",
                        "message", "End time cannot be before start time"
                ));
            }

            List<Booking> savedBookings = new ArrayList<>();

            for (LocalDate d : bookingDates) {
                // Overlap check
                boolean exists = bookingRepo
                        .existsByDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndBookTypeAndSubType(
                                d, endTime, startTime, request.bookType, request.subType
                        );

                if (exists) {
                    return ResponseEntity.status(409).body(Map.of(
                            "success", false,
                            "error", "ALREADY_BOOKED",
                            "message", "Item already booked on " + d
                    ));
                }

                Booking booking = new Booking(
                        request.id + "_" + d, // to avoid duplicate ID on recurring
                        d,
                        startTime,
                        endTime,
                        request.bookType,
                        request.subType,
                        request.officeLocation,
                        request.building,
                        request.floor,
                        request.recurrence != null ? request.recurrence.type : RecurrenceType.NONE,
                        request.recurrence != null && request.recurrence.endDate != null
                                ? LocalDate.parse(request.recurrence.endDate) : null,
                        request.recurrence != null && request.recurrence.customDates != null
                                ? request.recurrence.customDates.stream().map(LocalDate::parse).toList()
                                : null,
                        "confirmed"
                );

                savedBookings.add(bookingRepo.save(booking));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "booking", savedBookings,
                    "message", "Booking successful"
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "error", "SERVER_ERROR",
                    "message", e.getMessage()
            ));
        }
    }
}

