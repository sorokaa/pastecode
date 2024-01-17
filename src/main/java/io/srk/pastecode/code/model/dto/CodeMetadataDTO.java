package io.srk.pastecode.code.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contains code metadata")
public class CodeMetadataDTO {

    @Schema(description = "Does code has password")
    boolean isSecured;
}
