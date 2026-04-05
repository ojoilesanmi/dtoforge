package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaClassCodeGeneratorTest {

    private JavaClassCodeGenerator generator;

    @BeforeEach
    void setUp() {
        JavaSyntaxRenderer renderer = new JavaSyntaxRenderer();
        generator = new JavaClassCodeGenerator(renderer);
    }

    @Test
    void generatesSimpleClass() {
        ObjectType rootType = new ObjectType("UserDto", List.of(
                new FieldDefinition("name", "name", new PrimitiveType(JavaTypeKind.STRING)),
                new FieldDefinition("age", "age", new PrimitiveType(JavaTypeKind.INTEGER))
        ));

        GenerationOptions options = new GenerationOptions(EnumSet.noneOf(GenerationFlag.class), "com.example.dto");
        String result = generator.generate(rootType, options);

        assertTrue(result.contains("public class UserDto {"));
        assertTrue(result.contains("private String name;"));
        assertTrue(result.contains("private Integer age;"));
        assertTrue(result.contains("public String getName()"));
        assertTrue(result.contains("public void setName(String name)"));
        assertTrue(result.contains("public Integer getAge()"));
        assertTrue(result.contains("public void setAge(Integer age)"));
    }

    @Test
    void generatesNestedClasses() {
        ObjectType addressType = new ObjectType("AddressDto", List.of(
                new FieldDefinition("city", "city", new PrimitiveType(JavaTypeKind.STRING))
        ));

        ObjectType rootType = new ObjectType("UserDto", List.of(
                new FieldDefinition("name", "name", new PrimitiveType(JavaTypeKind.STRING)),
                new FieldDefinition("address", "address", addressType)
        ));

        GenerationOptions options = new GenerationOptions(EnumSet.noneOf(GenerationFlag.class), "com.example.dto");
        String result = generator.generate(rootType, options);

        assertTrue(result.contains("public class UserDto {"));
        assertTrue(result.contains("public class AddressDto {"));
        assertTrue(result.contains("private AddressDto address;"));
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
    }

    @Test
    void supportedStyleReturnsClass() {
        assertEquals(OutputStyle.CLASS, generator.supportedStyle());
    }
}
