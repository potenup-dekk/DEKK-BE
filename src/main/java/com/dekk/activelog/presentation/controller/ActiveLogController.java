package com.dekk.activelog.presentation.controller;

import com.dekk.activelog.application.ActiveLogCommandService;
import com.dekk.activelog.application.dto.command.SwipeCommand;
import com.dekk.activelog.presentation.request.SwipeRequest;
import com.dekk.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/cards")
@RequiredArgsConstructor
public class ActiveLogController implements ActiveLogApi {

    private final ActiveLogCommandService activeLogCommandService;

    @Override
    @PostMapping("/{cardId}/swipe")
    public ApiResponse<Void> swipeCard(
        @PathVariable("cardId") Long cardId,
        @Valid @RequestBody SwipeRequest request
    ) {
        // TODO: Spring Security 적용 후 SecurityContext에서 실제 userId 추출
        Long userId = null;

        if (userId == null) {
            return new ApiResponse<>("SAL20001", "비회원 스와이프 액션 (수집 생략)", null);
        }

        SwipeCommand command = new SwipeCommand(userId, cardId, request.swipeType());
        activeLogCommandService.saveSwipeAction(command);

        return new ApiResponse<>("SAL20001", "스와이프 평가가 정상적으로 기록되었습니다.", null);
    }
}
