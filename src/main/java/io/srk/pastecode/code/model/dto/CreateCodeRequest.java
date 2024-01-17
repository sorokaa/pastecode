package io.srk.pastecode.code.model.dto;

import io.srk.pastecode.code.model.enumeration.Availability;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@Schema(description = "Create code payload")
public class CreateCodeRequest {

    @Schema(description = "Code snippet")
    private String snippet;

    @Schema(description = "Code availability")
    private Availability availability;

    @Schema(description = "Code expiration datetime")
    private Instant expireAt;

    @Schema(description = "Code password")
    @ToString.Exclude
    private String password;
}
