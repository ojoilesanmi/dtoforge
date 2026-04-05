package io.github.ojoilesanmi.dtoforge.core.strategy;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class JavaNamingStrategy {

    private static final Set<String> RESERVED_KEYWORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while",
            "true", "false", "null"
    );

    private static final Pattern WORD_BOUNDARY = Pattern.compile("[_\\-\\s]+([a-zA-Z0-9])");
    private static final Pattern LEADING_NON_ALPHA = Pattern.compile("^[^a-zA-Z]+");

    public String toClassName(String raw) {
        String result = toPascalCase(raw);
        if (result.isEmpty()) {
            result = "Unnamed";
        }
        return escapeKeyword(result);
    }

    public String toFieldName(String raw) {
        String result = toCamelCase(raw);
        if (result.isEmpty()) {
            result = "unnamed";
        }
        return escapeKeyword(result);
    }

    public boolean isReservedKeyword(String name) {
        return RESERVED_KEYWORDS.contains(name.toLowerCase());
    }

    public String escapeKeyword(String name) {
        if (isReservedKeyword(name)) {
            return name + "Field";
        }
        return name;
    }

    private String toPascalCase(String raw) {
        String cleaned = normalize(raw);
        if (cleaned.isEmpty()) return "";

        Matcher matcher = WORD_BOUNDARY.matcher(cleaned);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            result.append(cleaned, lastEnd, matcher.start());
            result.append(matcher.group(1).toUpperCase());
            lastEnd = matcher.end();
        }
        result.append(cleaned.substring(lastEnd));

        String transformed = result.toString();
        if (transformed.isEmpty()) return "";

        return Character.toUpperCase(transformed.charAt(0)) + transformed.substring(1);
    }

    private String toCamelCase(String raw) {
        String pascal = toPascalCase(raw);
        if (pascal.isEmpty()) return "";
        return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
    }

    private String normalize(String raw) {
        String result = LEADING_NON_ALPHA.matcher(raw).replaceAll("");
        return result.replaceAll("[^a-zA-Z0-9_\\-\\s]", "");
    }
}
