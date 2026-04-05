package io.github.ojoilesanmi.dtoforge.web.dto;

import io.github.ojoilesanmi.dtoforge.core.model.GenerationFlag;
import io.github.ojoilesanmi.dtoforge.core.model.GenerationOptions;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.core.model.OutputStyle;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidOutputStyleException;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class GenerateCommandMapper {

    public GenerateCommand toCommand(GenerateDtoRequest request) {
        OutputStyle outputStyle = parseOutputStyle(request.outputStyle());

        var flags = EnumSet.noneOf(GenerationFlag.class);
        if (Boolean.TRUE.equals(request.useJacksonAnnotations())) {
            flags.add(GenerationFlag.JACKSON_ANNOTATIONS);
        }

        String packageName = request.packageName() != null && !request.packageName().isBlank()
                ? request.packageName()
                : null;

        GenerationOptions options = new GenerationOptions(flags, packageName);

        return new GenerateCommand(
                request.json(),
                request.rootClassName(),
                outputStyle,
                options
        );
    }

    private OutputStyle parseOutputStyle(String raw) {
        try {
            return OutputStyle.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOutputStyleException(raw);
        }
    }
}
