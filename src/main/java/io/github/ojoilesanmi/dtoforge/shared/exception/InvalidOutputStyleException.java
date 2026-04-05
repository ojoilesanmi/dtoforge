package io.github.ojoilesanmi.dtoforge.shared.exception;

public class InvalidOutputStyleException extends RuntimeException {
    public InvalidOutputStyleException(String style) {
        super("Invalid output style: " + style + ". Must be RECORD or CLASS.");
    }
}
