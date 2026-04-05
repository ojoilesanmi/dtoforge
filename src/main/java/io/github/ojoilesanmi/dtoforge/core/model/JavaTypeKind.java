package io.github.ojoilesanmi.dtoforge.core.model;

public enum JavaTypeKind {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    BIG_INTEGER("BigInteger"),
    DOUBLE("Double"),
    BIG_DECIMAL("BigDecimal"),
    BOOLEAN("Boolean"),
    OBJECT("Object");

    private final String javaTypeName;

    JavaTypeKind(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    public String javaTypeName() {
        return javaTypeName;
    }
}
