package io.srk.pastecode.code.service;

import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.dto.CodeMetadataDTO;
import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.dto.GetCodeByIdRequest;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.mapper.CodeMapper;
import io.srk.pastecode.code.repository.CodeRepository;
import io.srk.pastecode.code.util.CodeValidator;
import io.srk.pastecode.user.service.UserService;
import io.srk.pastecode.user.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository repository;
    private final CodeMapper mapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<CodeDTO> getLatestCodes() {
        return repository.findAllLatest().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CodeDTO> getByUser(Pageable pageable) {
        UUID userId = SecurityUtil.getCurrentUserId();
        return repository.findAllByOwnerId(userId, pageable)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public CodeDTO getById(UUID id, GetCodeByIdRequest request) {
        Code code = getEntityById(id);
        CodeValidator.validateGetById(code, request);
        return mapper.toDto(code);
    }

    @Transactional(readOnly = true)
    public CodeMetadataDTO getCodeMetadata(UUID id) {
        return repository.findMetadataById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public CodeDTO create(CreateCodeRequest request) {
        CodeValidator.validateCreate(request);
        Code entity = mapper.toEntity(request);
        entity.setOwner(userService.getCurrentUser());
        repository.save(entity);
        return mapper.toDto(entity);
    }

    private Code getEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
