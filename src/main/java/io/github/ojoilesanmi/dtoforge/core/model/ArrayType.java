package io.github.ojoilesanmi.dtoforge.core.model;

public record ArrayType(DomainType elementType) implements DomainType {
    @Override
    public String typeName() {
        return "List<" + elementType.typeName() + ">";
    }
}
