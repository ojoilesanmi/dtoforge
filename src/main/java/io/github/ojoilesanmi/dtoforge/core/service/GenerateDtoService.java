package io.github.ojoilesanmi.dtoforge.core.service;

import io.github.ojoilesanmi.dtoforge.core.generator.CodeGenerator;
import io.github.ojoilesanmi.dtoforge.core.generator.CodeGeneratorRegistry;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateResult;
import io.github.ojoilesanmi.dtoforge.core.model.ObjectType;
import io.github.ojoilesanmi.dtoforge.core.parser.JsonStructureParser;
import io.github.ojoilesanmi.dtoforge.core.validator.JsonInputValidator;
import org.springframework.stereotype.Service;

@Service
public class GenerateDtoService {

    private final JsonInputValidator validator;
    private final JsonStructureParser parser;
    private final CodeGeneratorRegistry generatorRegistry;

    public GenerateDtoService(JsonInputValidator validator,
                              JsonStructureParser parser,
                              CodeGeneratorRegistry generatorRegistry) {
        this.validator = validator;
        this.parser = parser;
        this.generatorRegistry = generatorRegistry;
    }

    public GenerateResult generate(GenerateCommand command) {
        validator.validate(command);

        ObjectType rootType = parser.parse(command.json(), command.rootClassName());

        CodeGenerator generator = generatorRegistry.get(command.outputStyle());
        String rawCode = generator.generate(rootType, command.options());

        return new GenerateResult(rawCode);
    }
}
