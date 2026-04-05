package io.github.ojoilesanmi.dtoforge.core.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ojoilesanmi.dtoforge.core.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TypeInferenceService {

    private final JavaNamingStrategy namingStrategy;

    public TypeInferenceService(JavaNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public DomainType infer(JsonNode node, String fieldName) {
        if (node == null || node.isNull()) {
            return new PrimitiveType(JavaTypeKind.OBJECT);
        }

        if (node.isTextual()) {
            return new PrimitiveType(JavaTypeKind.STRING);
        }

        if (node.isBoolean()) {
            return new PrimitiveType(JavaTypeKind.BOOLEAN);
        }

        if (node.isNumber()) {
            return new PrimitiveType(resolveNumberKind(node));
        }

        if (node.isArray()) {
            return inferArrayType(node, fieldName);
        }

        if (node.isObject()) {
            return inferObjectType(node, fieldName);
        }

        return new PrimitiveType(JavaTypeKind.OBJECT);
    }

    private DomainType inferArrayType(JsonNode node, String fieldName) {
        if (node.isEmpty()) {
            return new ArrayType(new PrimitiveType(JavaTypeKind.OBJECT));
        }

        JsonNode firstElement = node.get(0);

        if (firstElement.isObject()) {
            DomainType elementType = inferObjectType(firstElement, fieldName);
            return new ArrayType(elementType);
        }

        JavaTypeKind commonKind = findCommonPrimitiveKind(node);
        return new ArrayType(new PrimitiveType(commonKind));
    }

    private ObjectType inferObjectType(JsonNode node, String fieldName) {
        ObjectNode objectNode = (ObjectNode) node;
        List<FieldDefinition> fields = new ArrayList<>();
        String className = namingStrategy.toClassName(fieldName);

        for (Map.Entry<String, JsonNode> entry : objectNode.properties()) {
            String jsonName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            String javaName = namingStrategy.toFieldName(jsonName);
            DomainType fieldType = infer(fieldValue, jsonName);

            fields.add(new FieldDefinition(jsonName, javaName, fieldType));
        }

        return new ObjectType(className, fields);
    }

    private JavaTypeKind resolveNumberKind(JsonNode node) {
        if (node.isBigInteger()) return JavaTypeKind.BIG_INTEGER;
        if (node.isBigDecimal()) return JavaTypeKind.BIG_DECIMAL;
        if (node.isLong() && !node.isInt()) return JavaTypeKind.LONG;
        if (node.isDouble() || node.isFloat()) return JavaTypeKind.DOUBLE;
        if (node.isInt()) return JavaTypeKind.INTEGER;
        return JavaTypeKind.DOUBLE;
    }

    private JavaTypeKind findCommonPrimitiveKind(JsonNode arrayNode) {
        JavaTypeKind commonKind = null;

        for (JsonNode element : arrayNode) {
            JavaTypeKind elementKind;

            if (element.isTextual()) {
                elementKind = JavaTypeKind.STRING;
            } else if (element.isBoolean()) {
                elementKind = JavaTypeKind.BOOLEAN;
            } else if (element.isNumber()) {
                elementKind = resolveNumberKind(element);
            } else {
                return JavaTypeKind.OBJECT;
            }

            if (commonKind == null) {
                commonKind = elementKind;
            } else if (commonKind != elementKind) {
                commonKind = widenType(commonKind, elementKind);
            }
        }

        return commonKind != null ? commonKind : JavaTypeKind.OBJECT;
    }

    private JavaTypeKind widenType(JavaTypeKind a, JavaTypeKind b) {
        if (a == b) return a;

        List<JavaTypeKind> numericHierarchy = List.of(
                JavaTypeKind.INTEGER,
                JavaTypeKind.LONG,
                JavaTypeKind.BIG_INTEGER,
                JavaTypeKind.DOUBLE,
                JavaTypeKind.BIG_DECIMAL
        );

        int indexA = numericHierarchy.indexOf(a);
        int indexB = numericHierarchy.indexOf(b);

        if (indexA >= 0 && indexB >= 0) {
            return numericHierarchy.get(Math.max(indexA, indexB));
        }

        return JavaTypeKind.OBJECT;
    }
}
