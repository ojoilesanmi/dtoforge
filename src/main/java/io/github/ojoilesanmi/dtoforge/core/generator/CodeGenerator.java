package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.GenerationOptions;
import io.github.ojoilesanmi.dtoforge.core.model.ObjectType;
import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;

public interface CodeGenerator {
    OutputStyle supportedStyle();
    String generate(ObjectType rootType, GenerationOptions options);
}
