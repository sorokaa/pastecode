package io.srk.pastecode.code.model.dto;

import io.srk.pastecode.code.model.enumeration.Availability;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Schema(name = "CodeDTO", description = "Contains code's data")
public class CodeDTO {

    @Schema(description = "Code identifier")
    private UUID id;

    @Schema(description = "Code snippet")
    private String snippet;

    @Schema(description = "ID of user who owns this code")
    private UUID ownerId;

    @Schema(description = "Code expiration datetime")
    private Instant expireAt;

    @Schema(description = "Code availability")
    private Availability availability;

    @Schema(description = "Code creation datetime")
    private Instant created;
}
