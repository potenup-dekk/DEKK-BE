package com.dekk.app.card.recommend.presentation.controller;

import com.dekk.app.card.recommend.application.RecommendQueryService;
import com.dekk.app.card.recommend.presentation.dto.response.RecommendCardResponse;
import com.dekk.app.card.recommend.presentation.response.RecommendResultCode;
import com.dekk.global.response.ApiResponse;
import com.dekk.global.response.PageResponse;
import com.dekk.global.security.oauth2.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v2/cards")
@RequiredArgsConstructor
public class RecommendQueryController implements RecommendQueryApi {

    private final RecommendQueryService recommendQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RecommendCardResponse>>> getRecommendCards(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "10") int size) {
        List<RecommendCardResponse> cards =
                RecommendCardResponse.from(recommendQueryService.getRecommendCards(userDetails.getId(), size));

        return ResponseEntity.ok(
                ApiResponse.of(RecommendResultCode.RECOMMEND_CARD_SUCCESS, PageResponse.from(cards, size)));
    }
}
