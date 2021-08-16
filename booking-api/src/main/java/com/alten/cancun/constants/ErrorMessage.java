package com.alten.cancun.constants;

public enum ErrorMessage {

    LIMIT_OF_DAYS_IN_ADVANCED_WHEN_BOOKING("Reservation must not be more than 30 days in advanced"),
    START_DAY_AFTER_BOOKING("Reservation must start at least int the next day after booking."),
    LIMIT_OF_DAYS_FOR_RESERVATION("The amount of reservation days must not exceed 3 days"),
    PERIOD_NOT_DEFINED("The period of reservation must be defined"),
    CHECK_IN_OR_CHECK_OUT_NOT_DEFINED("The checkin and checkout dates must be defined"),
    CHECK_OUT_BEFORE_CHECK_IN("The checkout date must be after checkin"),
    PERIOD_ALREADY_BOOKED("The period you have chosen is already booked."),
    RESERVATION_NOT_FOUND("Reservation not found!");

    private String message;

    private ErrorMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
