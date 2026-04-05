package io.github.ojoilesanmi.dtoforge.web.controller;

import io.github.ojoilesanmi.dtoforge.core.model.GenerateCommand;
import io.github.ojoilesanmi.dtoforge.core.model.GenerateResult;
import io.github.ojoilesanmi.dtoforge.core.service.GenerateDtoService;
import io.github.ojoilesanmi.dtoforge.web.dto.GenerateCommandMapper;
import io.github.ojoilesanmi.dtoforge.web.dto.GenerateDtoRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dto-generator")
public class DtoGeneratorController {

    private final GenerateDtoService generateDtoService;
    private final GenerateCommandMapper commandMapper;

    public DtoGeneratorController(GenerateDtoService generateDtoService, GenerateCommandMapper commandMapper) {
        this.generateDtoService = generateDtoService;
        this.commandMapper = commandMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@Valid @RequestBody GenerateDtoRequest request) {
        GenerateCommand command = commandMapper.toCommand(request);
        GenerateResult result = generateDtoService.generate(command);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(result.generatedCode());
    }
}
