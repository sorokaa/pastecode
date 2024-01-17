package io.srk.pastecode.code.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Code access request")
public class GetCodeByIdRequest {

    @Schema(description = "Code password (if it has any)")
    private String password;
}
