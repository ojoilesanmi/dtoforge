package io.github.ojoilesanmi.dtoforge.core.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ojoilesanmi.dtoforge.core.model.*;
import io.github.ojoilesanmi.dtoforge.core.strategy.JavaNamingStrategy;
import io.github.ojoilesanmi.dtoforge.core.strategy.TypeInferenceService;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonJsonStructureParserTest {

    private JacksonJsonStructureParser parser;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaNamingStrategy namingStrategy = new JavaNamingStrategy();
        TypeInferenceService typeInferenceService = new TypeInferenceService(namingStrategy);
        parser = new JacksonJsonStructureParser(objectMapper, typeInferenceService, namingStrategy);
    }

    @Test
    void parsesSimpleObject() {
        String json = "{\"name\": \"John\", \"age\": 30}";
        ObjectType result = parser.parse(json, "UserDto");

        assertEquals("UserDto", result.name());
        assertEquals(2, result.fields().size());
    }

    @Test
    void parsesNestedObject() {
        String json = "{\"name\": \"John\", \"address\": {\"city\": \"London\"}}";
        ObjectType result = parser.parse(json, "UserDto");

        assertEquals("UserDto", result.name());

        FieldDefinition addressField = result.fields().stream()
                .filter(f -> f.javaName().equals("address"))
                .findFirst()
                .orElseThrow();

        assertInstanceOf(ObjectType.class, addressField.type());
        ObjectType address = (ObjectType) addressField.type();
        assertEquals("Address", address.name());
    }

    @Test
    void parsesArrayField() {
        String json = "{\"name\": \"John\", \"tags\": [\"a\", \"b\"]}";
        ObjectType result = parser.parse(json, "UserDto");

        FieldDefinition tagsField = result.fields().stream()
                .filter(f -> f.javaName().equals("tags"))
                .findFirst()
                .orElseThrow();

        assertInstanceOf(ArrayType.class, tagsField.type());
    }

    @Test
    void throwsOnInvalidJson() {
        assertThrows(InvalidJsonException.class, () -> parser.parse("not json", "Test"));
    }

    @Test
    void throwsOnNonObjectRoot() {
        assertThrows(InvalidJsonStructureException.class, () -> parser.parse("[1, 2, 3]", "Test"));
    }

    @Test
    void mapsFieldNamesCorrectly() {
        String json = "{\"user_name\": \"John\", \"user-profile\": \"dev\"}";
        ObjectType result = parser.parse(json, "UserDto");

        assertTrue(result.fields().stream().anyMatch(f -> f.javaName().equals("userName")));
        assertTrue(result.fields().stream().anyMatch(f -> f.javaName().equals("userProfile")));
    }
}
