package com.dekk.app.user.presentation.response;

import com.dekk.app.user.application.dto.result.UserInfoResult;
import com.dekk.app.user.domain.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserInfoResponse(
        Long id,
        String email,
        String nickname,
        Integer height,
        Integer weight,
        Gender gender,
        String status,
        String role) {
    public static UserInfoResponse from(UserInfoResult result) {
        return new UserInfoResponse(
                result.id(),
                result.email(),
                result.nickname(),
                result.height(),
                result.weight(),
                result.gender(),
                result.status(),
                result.role());
    }
}
