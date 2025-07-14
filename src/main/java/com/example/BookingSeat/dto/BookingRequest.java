package com.example.BookingSeat.dto;



import com.example.BookingSeat.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    public String id;
    public String date;
    public String startTime;
    public String endTime;
    public Booking.BookType bookType;
    public String subType;
    public String officeLocation;
    public String building;
    public String floor;

    public Recurrence recurrence;

    public static class Recurrence {
        public Booking.RecurrenceType type;
        public String endDate;
        public List<String> customDates;
    }
}