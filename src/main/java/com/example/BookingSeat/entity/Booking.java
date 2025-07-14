package com.example.BookingSeat.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    private String id;  // you can set this from frontend (UUID or custom)

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private BookType bookType; // DESK or RESOURCE

    private String subType; // null for DESK, else MEETING_ROOM, PROJECTOR, etc.

    private String officeLocation;
    private String building;
    private String floor;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType; // NONE, DAILY, WEEKLY, CUSTOM

    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "booking_custom_dates", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "custom_date")
    private List<LocalDate> customDates;

    private String status; // e.g. "confirmed", "cancelled", etc.

    public Booking() {}

    // Constructor (you can use Lombok or generate via IDE)
    public Booking(String id, LocalDate date, LocalTime startTime, LocalTime endTime,
                   BookType bookType, String subType, String officeLocation, String building,
                   String floor, RecurrenceType recurrenceType, LocalDate endDate,
                   List<LocalDate> customDates, String status) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookType = bookType;
        this.subType = subType;
        this.officeLocation = officeLocation;
        this.building = building;
        this.floor = floor;
        this.recurrenceType = recurrenceType;
        this.endDate = endDate;
        this.customDates = customDates;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<LocalDate> getCustomDates() {
        return customDates;
    }

    public void setCustomDates(List<LocalDate> customDates) {
        this.customDates = customDates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public enum BookType {
        DESK,
        RESOURCE
    }
    public enum RecurrenceType {
        NONE,
        DAILY,
        WEEKLY,
        CUSTOM
    }
}
