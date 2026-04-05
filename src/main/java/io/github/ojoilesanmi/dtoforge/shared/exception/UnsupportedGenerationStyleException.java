package io.github.ojoilesanmi.dtoforge.shared.exception;

import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;

public class UnsupportedGenerationStyleException extends RuntimeException {
    public UnsupportedGenerationStyleException(OutputStyle style) {
        super("Unsupported output style: " + style);
    }
}
