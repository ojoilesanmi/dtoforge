package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static io.github.ojoilesanmi.dtoforge.shared.JavaKeywords.*;

@Component
public class JavaClassCodeGenerator implements CodeGenerator {

    private final JavaSyntaxRenderer renderer;

    public JavaClassCodeGenerator(JavaSyntaxRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public OutputStyle supportedStyle() {
        return OutputStyle.CLASS;
    }

    @Override
    public String generate(ObjectType rootType, GenerationOptions options) {
        StringBuilder sb = new StringBuilder();

        sb.append(renderer.buildPackageLine(options.packageName()));
        sb.append(renderer.buildImports(renderer.resolveImports(rootType, options)));

        List<String> nestedTypes = new ArrayList<>();
        collectNestedTypes(rootType, options, nestedTypes);

        for (String nested : nestedTypes) {
            sb.append(nested).append("\n\n");
        }
        sb.append(buildType(rootType, options)).append("\n");

        return sb.toString();
    }

    private void collectNestedTypes(ObjectType type, GenerationOptions options, List<String> nestedTypes) {
        for (FieldDefinition field : type.fields()) {
            if (field.type() instanceof ObjectType nested) {
                collectNestedTypes(nested, options, nestedTypes);
                nestedTypes.add(buildType(nested, options));
            }
        }
    }

    private String buildType(ObjectType objectType, GenerationOptions options) {
        String indent = renderer.indent();
        StringBuilder sb = new StringBuilder();

        sb.append(PUBLIC).append(" ").append(CLASS).append(" ");
        sb.append(objectType.name());
        sb.append(" {\n");

        for (FieldDefinition field : objectType.fields()) {
            if (options.hasFlag(GenerationFlag.JACKSON_ANNOTATIONS)) {
                sb.append(renderer.buildJacksonAnnotation(field.jsonName()));
            }

            sb.append(indent);
            sb.append(PRIVATE).append(" ");
            sb.append(renderer.resolveTypeName(field.type()));
            sb.append(" ");
            sb.append(field.javaName());
            sb.append(";\n");
        }

        sb.append("\n");

        for (FieldDefinition field : objectType.fields()) {
            String typeStr = renderer.resolveTypeName(field.type());
            String fieldName = field.javaName();
            String capitalized = capitalize(fieldName);

            sb.append(indent);
            sb.append(PUBLIC).append(" ");
            sb.append(typeStr);
            sb.append(" get");
            sb.append(capitalized);
            sb.append("() {\n");
            sb.append(indent).append(indent);
            sb.append(RETURN).append(" ");
            sb.append(fieldName);
            sb.append(";\n");
            sb.append(indent);
            sb.append("}\n\n");

            sb.append(indent);
            sb.append(PUBLIC).append(" ").append(VOID).append(" set");
            sb.append(capitalized);
            sb.append("(");
            sb.append(typeStr);
            sb.append(" ");
            sb.append(fieldName);
            sb.append(") {\n");
            sb.append(indent).append(indent);
            sb.append(THIS).append(".");
            sb.append(fieldName);
            sb.append(" = ");
            sb.append(fieldName);
            sb.append(";\n");
            sb.append(indent);
            sb.append("}\n\n");
        }

        sb.append("}");

        return sb.toString();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
