package io.github.ojoilesanmi.dtoforge.web.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ojoilesanmi.dtoforge.core.model.GenerationFlag;
import io.github.ojoilesanmi.dtoforge.core.model.GenerationOptions;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidOutputStyleException;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class GenerateCommandMapper {

    private final ObjectMapper objectMapper;

    public GenerateCommandMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GenerateCommand toCommand(GenerateDtoRequest request) {
        String json = resolveJsonString(request.json());
        OutputStyle outputStyle = parseOutputStyle(request.outputStyle());

        var flags = EnumSet.noneOf(GenerationFlag.class);
        if (Boolean.TRUE.equals(request.useJacksonAnnotations())) {
            flags.add(GenerationFlag.JACKSON_ANNOTATIONS);
        }

        String packageName = request.packageName() != null && !request.packageName().isBlank()
                ? request.packageName()
                : null;

        return new GenerateCommand(
                json,
                request.rootClassName(),
                outputStyle,
                new GenerationOptions(flags, packageName)
        );
    }

    private String resolveJsonString(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        }
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new InvalidJsonException("Failed to serialize JSON node: " + e.getMessage());
        }
    }

    private OutputStyle parseOutputStyle(String raw) {
        if (raw == null || raw.isBlank()) {
            return OutputStyle.RECORD;
        }
        try {
            return OutputStyle.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOutputStyleException(raw);
        }
    }
}
