package de.sourcepark.dissplayer.exceptions;

/**
 * Created by jnaperkowski on 11.12.15.
 */
public class SlotNotFoundException extends Exception {
    public SlotNotFoundException() {}

    public SlotNotFoundException(String message) {
        super(message);
    }

    public SlotNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
