package io.github.ojoilesanmi.dtoforge.core.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonStructureException;
import org.springframework.stereotype.Component;

@Component
public class JsonInputValidator {

    private final ObjectMapper objectMapper;

    public JsonInputValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void validate(GenerateCommand command) {
        try {
            JsonNode rootNode = objectMapper.readTree(command.json());
            if (!rootNode.isObject()) {
                throw new InvalidJsonStructureException("Root JSON must be an object");
            }
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Invalid JSON: " + e.getMessage());
        }
    }
}
