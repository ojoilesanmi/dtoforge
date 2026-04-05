package io.github.ojoilesanmi.dtoforge.core.model;

import java.util.List;

public record ObjectType(String name, List<FieldDefinition> fields) implements DomainType {
    @Override
    public String typeName() {
        return name;
    }
}
