package com.differz.bc.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginInputDto {
    @NotNull
    @Size(max = 30)
    @JsonProperty("user_name")
    private String username;
    @NotNull
    @Size(max = 60)
    @JsonProperty("password")
    private String password;
}
