package io.github.ojoilesanmi.dtoforge.core.generator;

import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static io.github.ojoilesanmi.dtoforge.shared.JavaKeywords.*;

@Component
public class JavaRecordCodeGenerator implements CodeGenerator {

    private final JavaSyntaxRenderer renderer;

    public JavaRecordCodeGenerator(JavaSyntaxRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public OutputStyle supportedStyle() {
        return OutputStyle.RECORD;
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

        sb.append(PUBLIC).append(" ").append(RECORD).append(" ");
        sb.append(objectType.name());
        sb.append("(\n");

        List<FieldDefinition> fields = objectType.fields();
        for (int i = 0; i < fields.size(); i++) {
            FieldDefinition field = fields.get(i);

            if (options.hasFlag(GenerationFlag.JACKSON_ANNOTATIONS)) {
                sb.append(renderer.buildJacksonAnnotation(field.jsonName()));
            }

            sb.append(indent);
            sb.append(renderer.resolveTypeName(field.type()));
            sb.append(" ");
            sb.append(field.javaName());

            if (i < fields.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(") {}");

        return sb.toString();
    }
}
