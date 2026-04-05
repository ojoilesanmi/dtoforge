package io.github.ojoilesanmi.dtoforge.core.parser;

import io.github.ojoilesanmi.dtoforge.core.model.ObjectType;

public interface JsonStructureParser {
    ObjectType parse(String json, String rootClassName);
}
