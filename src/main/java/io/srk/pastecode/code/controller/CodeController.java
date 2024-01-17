package io.srk.pastecode.code.controller;

import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.dto.CodeMetadataDTO;
import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.dto.GetCodeByIdRequest;
import io.srk.pastecode.code.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
@Tag(name = "Code API")
public class CodeController {

    private final CodeService service;

    @Operation(summary = "Returns latest 10 codes")
    @GetMapping
    public List<CodeDTO> getLatest() {
        log.debug("API Request to get latest codes");
        return service.getLatestCodes();
    }

    @Operation(summary = "Returns user's codes")
    @GetMapping("/by-user")
    @PageableAsQueryParam
    public Page<CodeDTO> getByUser(@ParameterObject @PageableDefault Pageable pageable) {
        log.debug("API Request to get codes by user");
        return service.getByUser(pageable);
    }

    @Operation(summary = "Returns code's metadata")
    @GetMapping("/{id}/metadata")
    public CodeMetadataDTO getCodeMetadata(@PathVariable UUID id) {
        log.debug("API Request to get code metadata by id: {}", id);
        return service.getCodeMetadata(id);
    }

    @Operation(summary = "Return's code by id")
    @PostMapping("/{id}")
    public CodeDTO getById(
            @PathVariable UUID id,
            @RequestBody(required = false) GetCodeByIdRequest request
    ) {
        log.debug("API Request to get code by id: {}", request);
        return service.getById(id, request);
    }

    @Operation(summary = "Creates code")
    @PostMapping
    public CodeDTO create(@RequestBody CreateCodeRequest request) {
        log.debug("API Request to create code. Request: {}", request);
        return service.create(request);
    }
}
