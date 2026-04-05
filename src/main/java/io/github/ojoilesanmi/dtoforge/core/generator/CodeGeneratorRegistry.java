package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;
import io.github.ojoilesanmi.dtoforge.shared.exception.UnsupportedGenerationStyleException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CodeGeneratorRegistry {

    private final Map<OutputStyle, CodeGenerator> generators;

    public CodeGeneratorRegistry(List<CodeGenerator> generatorBeans) {
        this.generators = generatorBeans.stream()
                .collect(Collectors.toMap(CodeGenerator::supportedStyle, Function.identity()));
    }

    public CodeGenerator get(OutputStyle style) {
        return Optional.ofNullable(generators.get(style))
                .orElseThrow(() -> new UnsupportedGenerationStyleException(style));
    }
}
