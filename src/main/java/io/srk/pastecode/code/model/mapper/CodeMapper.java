package io.srk.pastecode.code.model.mapper;

import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.entity.Code;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CodeMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    CodeDTO toDto(Code code);

    @Mapping(target = "password", source = "password", qualifiedByName = "getEncryptedPassword")
    Code toEntity(CreateCodeRequest request);

    @Named("getEncryptedPassword")
    default String getEncryptedPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
