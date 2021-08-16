package com.alten.cancun.constants;

public enum ErrorMessage {

    ROOM_NOT_FOUND("Room not found!"),
    RESERVATION_NOT_FOUND("Reservation not found!");

    private String message;

    private ErrorMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
