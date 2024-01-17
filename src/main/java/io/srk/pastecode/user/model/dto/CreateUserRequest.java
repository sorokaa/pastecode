package io.srk.pastecode.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@Schema(description = "Create user payload")
public class CreateUserRequest {

    @Schema(description = "User's username")
    private String username;

    @Schema(description = "User's password")
    @ToString.Exclude
    private String password;
}
