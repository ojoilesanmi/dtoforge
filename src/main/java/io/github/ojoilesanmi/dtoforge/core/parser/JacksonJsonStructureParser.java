package io.github.ojoilesanmi.dtoforge.core.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ojoilesanmi.dtoforge.core.model.*;
import io.github.ojoilesanmi.dtoforge.core.strategy.JavaNamingStrategy;
import io.github.ojoilesanmi.dtoforge.core.strategy.TypeInferenceService;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonStructureException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JacksonJsonStructureParser implements JsonStructureParser {

    private final ObjectMapper objectMapper;
    private final TypeInferenceService typeInferenceService;
    private final JavaNamingStrategy namingStrategy;

    public JacksonJsonStructureParser(ObjectMapper objectMapper, TypeInferenceService typeInferenceService, JavaNamingStrategy namingStrategy) {
        this.objectMapper = objectMapper;
        this.typeInferenceService = typeInferenceService;
        this.namingStrategy = namingStrategy;
    }

    @Override
    public ObjectType parse(String json, String rootClassName) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Failed to parse JSON: " + e.getMessage());
        }

        if (!rootNode.isObject()) {
            throw new InvalidJsonStructureException("Root JSON must be an object");
        }

        return mapObject(rootNode, rootClassName);
    }

    private ObjectType mapObject(JsonNode node, String className) {
        ObjectNode objectNode = (ObjectNode) node;
        List<FieldDefinition> fields = new ArrayList<>();

        for (Map.Entry<String, JsonNode> entry : objectNode.properties()) {
            String jsonName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            DomainType fieldType = typeInferenceService.infer(fieldValue, jsonName);
            String javaName = namingStrategy.toFieldName(jsonName);

            fields.add(new FieldDefinition(jsonName, javaName, fieldType));
        }

        return new ObjectType(className, fields);
    }
}
