package io.github.ojoilesanmi.dtoforge.core.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeInferenceServiceTest {

    private TypeInferenceService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        JavaNamingStrategy namingStrategy = new JavaNamingStrategy();
        service = new TypeInferenceService(namingStrategy);
    }

    @Test
    void infersStringType() throws Exception {
        JsonNode node = objectMapper.readTree("\"hello\"");
        DomainType result = service.infer(node, "name");
        assertInstanceOf(PrimitiveType.class, result);
        assertEquals(JavaTypeKind.STRING, ((PrimitiveType) result).kind());
    }

    @Test
    void infersIntegerType() throws Exception {
        JsonNode node = objectMapper.readTree("42");
        DomainType result = service.infer(node, "age");
        assertInstanceOf(PrimitiveType.class, result);
        assertEquals(JavaTypeKind.INTEGER, ((PrimitiveType) result).kind());
    }

    @Test
    void infersBooleanType() throws Exception {
        JsonNode node = objectMapper.readTree("true");
        DomainType result = service.infer(node, "active");
        assertInstanceOf(PrimitiveType.class, result);
        assertEquals(JavaTypeKind.BOOLEAN, ((PrimitiveType) result).kind());
    }

    @Test
    void infersObjectType() throws Exception {
        JsonNode node = objectMapper.readTree("{\"city\": \"London\"}");
        DomainType result = service.infer(node, "address");
        assertInstanceOf(ObjectType.class, result);
        ObjectType obj = (ObjectType) result;
        assertEquals("Address", obj.name());
        assertEquals(1, obj.fields().size());
    }

    @Test
    void infersEmptyArrayType() throws Exception {
        JsonNode node = objectMapper.readTree("[]");
        DomainType result = service.infer(node, "items");
        assertInstanceOf(ArrayType.class, result);
        ArrayType arr = (ArrayType) result;
        assertInstanceOf(PrimitiveType.class, arr.elementType());
        assertEquals(JavaTypeKind.OBJECT, ((PrimitiveType) arr.elementType()).kind());
    }

    @Test
    void infersHomogeneousPrimitiveArray() throws Exception {
        JsonNode node = objectMapper.readTree("[\"a\", \"b\", \"c\"]");
        DomainType result = service.infer(node, "tags");
        assertInstanceOf(ArrayType.class, result);
        ArrayType arr = (ArrayType) result;
        assertInstanceOf(PrimitiveType.class, arr.elementType());
        assertEquals(JavaTypeKind.STRING, ((PrimitiveType) arr.elementType()).kind());
    }

    @Test
    void infersNullAsObject() throws Exception {
        JsonNode node = objectMapper.readTree("null");
        DomainType result = service.infer(node, "value");
        assertInstanceOf(PrimitiveType.class, result);
        assertEquals(JavaTypeKind.OBJECT, ((PrimitiveType) result).kind());
    }
}
