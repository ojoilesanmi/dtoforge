package io.github.ojoilesanmi.dtoforge.core.model;

public record PrimitiveType(JavaTypeKind kind) implements DomainType {
    @Override
    public String typeName() {
        return kind.javaTypeName();
    }
}
