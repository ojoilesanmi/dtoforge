package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JavaRecordCodeGeneratorTest {

    private JavaRecordCodeGenerator generator;

    @BeforeEach
    void setUp() {
        JavaSyntaxRenderer renderer = new JavaSyntaxRenderer();
        generator = new JavaRecordCodeGenerator(renderer);
    }

    @Test
    void generatesSimpleRecord() {
        ObjectType rootType = new ObjectType("UserDto", List.of(
                new FieldDefinition("name", "name", new PrimitiveType(JavaTypeKind.STRING)),
                new FieldDefinition("age", "age", new PrimitiveType(JavaTypeKind.INTEGER))
        ));

        GenerationOptions options = new GenerationOptions(EnumSet.noneOf(GenerationFlag.class), "com.example.dto");
        String result = generator.generate(rootType, options);

        assertTrue(result.contains("public record UserDto("));
        assertTrue(result.contains("String name"));
        assertTrue(result.contains("Integer age"));
        assertTrue(result.contains("package com.example.dto;"));
    }

    @Test
    void generatesNestedRecords() {
        ObjectType addressType = new ObjectType("AddressDto", List.of(
                new FieldDefinition("city", "city", new PrimitiveType(JavaTypeKind.STRING))
        ));

        ObjectType rootType = new ObjectType("UserDto", List.of(
                new FieldDefinition("name", "name", new PrimitiveType(JavaTypeKind.STRING)),
                new FieldDefinition("address", "address", addressType)
        ));

        GenerationOptions options = new GenerationOptions(EnumSet.noneOf(GenerationFlag.class), "com.example.dto");
        String result = generator.generate(rootType, options);

        assertTrue(result.contains("public record UserDto("));
        assertTrue(result.contains("public record AddressDto("));
        assertTrue(result.contains("AddressDto address"));
    }

    @Test
    void generatesWithJacksonAnnotations() {
        ObjectType rootType = new ObjectType("UserDto", List.of(
                new FieldDefinition("user_name", "userName", new PrimitiveType(JavaTypeKind.STRING))
        ));

        GenerationOptions options = new GenerationOptions(
                EnumSet.of(GenerationFlag.JACKSON_ANNOTATIONS),
                "com.example.dto"
        );
        String result = generator.generate(rootType, options);

        assertTrue(result.contains("@JsonProperty(\"user_name\")"));
        assertTrue(result.contains("import com.fasterxml.jackson.annotation.JsonProperty;"));
    }

    @Test
    void supportedStyleReturnsRecord() {
        assertEquals(OutputStyle.RECORD, generator.supportedStyle());
    }
}
