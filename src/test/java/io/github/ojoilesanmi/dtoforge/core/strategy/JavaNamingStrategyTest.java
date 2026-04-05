package io.github.ojoilesanmi.dtoforge.core.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaNamingStrategyTest {

    private JavaNamingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new JavaNamingStrategy();
    }

    @Test
    void toClassName_convertsSnakeCase() {
        assertEquals("UserName", strategy.toClassName("user_name"));
    }

    @Test
    void toClassName_convertsKebabCase() {
        assertEquals("UserProfile", strategy.toClassName("user-profile"));
    }

    @Test
    void toClassName_convertsSpaceSeparated() {
        assertEquals("UserDetails", strategy.toClassName("User Details"));
    }

    @Test
    void toClassName_handlesAlreadyPascalCase() {
        assertEquals("Address", strategy.toClassName("Address"));
    }

    @Test
    void toClassName_escapedReservedKeyword() {
        assertEquals("ClassField", strategy.toClassName("class"));
    }

    @Test
    void toFieldName_convertsSnakeCase() {
        assertEquals("userName", strategy.toFieldName("user_name"));
    }

    @Test
    void toFieldName_convertsKebabCase() {
        assertEquals("userProfile", strategy.toFieldName("user-profile"));
    }

    @Test
    void toFieldName_escapedReservedKeyword() {
        assertEquals("defaultField", strategy.toFieldName("default"));
    }

    @Test
    void isReservedKeyword_detectsKeywords() {
        assertTrue(strategy.isReservedKeyword("class"));
        assertTrue(strategy.isReservedKeyword("default"));
        assertTrue(strategy.isReservedKeyword("enum"));
        assertFalse(strategy.isReservedKeyword("className"));
    }

    @Test
    void toClassName_handlesEmptyInput() {
        assertEquals("Unnamed", strategy.toClassName(""));
    }

    @Test
    void toFieldName_handlesEmptyInput() {
        assertEquals("unnamed", strategy.toFieldName(""));
    }

    @Test
    void toClassName_stripsLeadingNonAlpha() {
        assertEquals("Field", strategy.toClassName("1_field"));
    }
}
