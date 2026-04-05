package io.github.ojoilesanmi.dtoforge.core.model;

import java.util.Objects;
import java.util.Set;

public record GenerationOptions(Set<GenerationFlag> flags, String packageName) {

    public GenerationOptions {
        Objects.requireNonNull(flags, "flags must not be null");
    }

    public boolean hasFlag(GenerationFlag flag) {
        return flags.contains(flag);
    }
}
