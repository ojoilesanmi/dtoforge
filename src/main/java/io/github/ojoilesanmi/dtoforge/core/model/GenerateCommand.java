package io.github.ojoilesanmi.dtoforge.core.model;

public record GenerateCommand(
        String json,
        String rootClassName,
        OutputStyle outputStyle,
        GenerationOptions options
) {}
