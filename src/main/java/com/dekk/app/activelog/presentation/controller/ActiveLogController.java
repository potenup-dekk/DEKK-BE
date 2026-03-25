package com.dekk.app.activelog.presentation.controller;

import com.dekk.app.activelog.application.ActiveLogCommandService;
import com.dekk.app.activelog.application.dto.command.SwipeCommand;
import com.dekk.app.activelog.presentation.request.SwipeRequest;
import com.dekk.app.activelog.presentation.response.ActiveLogResultCode;
import com.dekk.global.response.ApiResponse;
import com.dekk.global.security.oauth2.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActiveLogController implements ActiveLogApi {

    private final ActiveLogCommandService activeLogCommandService;

    @Override
    @PostMapping("/w/v1/cards/{cardId}/swipe")
    public ResponseEntity<ApiResponse<Void>> swipeCard(
            @PathVariable("cardId") Long cardId,
            @Valid @RequestBody SwipeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SwipeCommand command = new SwipeCommand(userDetails.getId(), cardId, request.swipeType());
        activeLogCommandService.saveSwipeAction(command);

        return ResponseEntity.ok(ApiResponse.from(ActiveLogResultCode.SWIPE_SUCCESS));
    }
}
