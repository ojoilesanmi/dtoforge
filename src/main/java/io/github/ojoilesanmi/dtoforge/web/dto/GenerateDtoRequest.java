package io.github.ojoilesanmi.dtoforge.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GenerateDtoRequest(
        @NotBlank(message = "JSON input must not be blank")
        String json,
        @NotBlank(message = "Root class name must not be blank")
        String rootClassName,
        @NotBlank(message = "Output style must be specified")
        @Pattern(regexp = "(?i)RECORD|CLASS", message = "Output style must be RECORD or CLASS")
        String outputStyle,
        Boolean useJacksonAnnotations,
        String packageName
) {}
