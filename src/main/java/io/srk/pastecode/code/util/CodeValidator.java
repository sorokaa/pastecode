package io.srk.pastecode.code.util;

import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.dto.GetCodeByIdRequest;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@UtilityClass
public class CodeValidator {

    public void validateCreate(CreateCodeRequest request) {
        validatePassword(request.getPassword());
    }

    public void validateGetById(Code code, GetCodeByIdRequest request) {
        if (code.isExpired()) {
            throw new BadRequestException(ExceptionCode.SNIPPET_IS_EXPIRED);
        }
        if (isWrongPasswordProvided(code, request)) {
            throw new BadRequestException(ExceptionCode.WRONG_PASSWORD_PROVIDED);
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            return;
        }
        if (StringUtils.isBlank(password)) {
            throw new BadRequestException(ExceptionCode.BLANK_PASSWORD_PROVIDED);
        }
    }

    private boolean isWrongPasswordProvided(Code code, GetCodeByIdRequest request) {
        if (code.getPassword() == null) {
            return false;
        }
        if (request.getPassword() == null) {
            return true;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return !encoder.matches(request.getPassword(), code.getPassword());
    }
}
