package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.*;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.ojoilesanmi.dtoforge.shared.JavaKeywords.*;

@Component
public class JavaSyntaxRenderer {

    private static final String INDENT = "    ";

    public String buildPackageLine(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            return "";
        }
        return PACKAGE + " " + packageName + ";\n\n";
    }

    public String buildImports(Set<String> importTypes) {
        if (importTypes.isEmpty()) {
            return "\n";
        }
        return importTypes.stream()
                .sorted()
                .map(type -> IMPORT + " " + type + ";")
                .collect(Collectors.joining("\n")) + "\n\n";
    }

    public String buildJacksonAnnotation(String jsonName) {
        return INDENT + "@" + JACKSON_PROPERTY + "(\"" + jsonName + "\")\n";
    }

    public String indent() {
        return INDENT;
    }

    public String resolveTypeName(DomainType type) {
        if (type instanceof ArrayType arr) {
            return LIST + "<" + resolveTypeName(arr.elementType()) + ">";
        }
        return type.typeName();
    }

    public Set<String> resolveImports(ObjectType rootType, GenerationOptions options) {
        Set<String> imports = new HashSet<>();

        if (options.hasFlag(GenerationFlag.JACKSON_ANNOTATIONS)) {
            imports.add(JACKSON_ANNOTATION_PKG + "." + JACKSON_PROPERTY);
        }

        collectImports(rootType, imports);
        return imports;
    }

    private void collectImports(DomainType type, Set<String> imports) {
        if (type instanceof PrimitiveType pt) {
            if (pt.kind() == JavaTypeKind.BIG_INTEGER) {
                imports.add(JAVA_MATH_BIG_INTEGER);
            } else if (pt.kind() == JavaTypeKind.BIG_DECIMAL) {
                imports.add(JAVA_MATH_BIG_DECIMAL);
            }
        } else if (type instanceof ArrayType arr) {
            imports.add(JAVA_UTIL_LIST);
            collectImports(arr.elementType(), imports);
        } else if (type instanceof ObjectType obj) {
            for (FieldDefinition field : obj.fields()) {
                collectImports(field.type(), imports);
            }
        }
    }
}
