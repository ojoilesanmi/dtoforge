package io.github.ojoilesanmi.dtoforge.web.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenerateDtoRequest(
        @NotNull(message = "JSON input must not be null")
        JsonNode json,
        @NotBlank(message = "Root class name must not be blank")
        String rootClassName,
        String outputStyle,
        Boolean useJacksonAnnotations,
        String packageName
) {}
