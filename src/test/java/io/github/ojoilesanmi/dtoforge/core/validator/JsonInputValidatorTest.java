package io.github.ojoilesanmi.dtoforge.core.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ojoilesanmi.dtoforge.core.model.GenerationOptions;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class JsonInputValidatorTest {

    private JsonInputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new JsonInputValidator(new ObjectMapper());
    }

    @Test
    void passesValidInput() {
        GenerateCommand command = new GenerateCommand(
                "{\"name\": \"test\"}",
                "TestDto",
                OutputStyle.RECORD,
                new GenerationOptions(EnumSet.noneOf(io.github.ojoilesanmi.dtoforge.core.model.GenerationFlag.class), null)
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void throwsOnInvalidJson() {
        GenerateCommand command = new GenerateCommand(
                "not json",
                "TestDto",
                OutputStyle.RECORD,
                new GenerationOptions(EnumSet.noneOf(io.github.ojoilesanmi.dtoforge.core.model.GenerationFlag.class), null)
        );

        assertThrows(InvalidJsonException.class, () -> validator.validate(command));
    }

    @Test
    void throwsOnNonObjectRoot() {
        GenerateCommand command = new GenerateCommand(
                "[1, 2, 3]",
                "TestDto",
                OutputStyle.RECORD,
                new GenerationOptions(EnumSet.noneOf(io.github.ojoilesanmi.dtoforge.core.model.GenerationFlag.class), null)
        );

        assertThrows(InvalidJsonStructureException.class, () -> validator.validate(command));
    }
}
