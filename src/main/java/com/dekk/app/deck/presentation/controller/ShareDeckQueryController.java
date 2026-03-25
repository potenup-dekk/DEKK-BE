package com.dekk.app.deck.presentation.controller;

import com.dekk.app.deck.application.ShareDeckQueryService;
import com.dekk.app.deck.presentation.response.DeckResultCode;
import com.dekk.app.deck.presentation.response.GuestSharedDeckCardResponse;
import com.dekk.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks/shared")
@RequiredArgsConstructor
public class ShareDeckQueryController implements ShareDeckQueryApi {

    private final ShareDeckQueryService shareDeckQueryService;

    @Override
    @GetMapping("/{token}/cards")
    public ResponseEntity<ApiResponse<List<GuestSharedDeckCardResponse>>> getSharedDeckPreview(
            @PathVariable("token") String token) {
        List<GuestSharedDeckCardResponse> response = shareDeckQueryService.getSharedDeckPreview(token).stream()
                .map(GuestSharedDeckCardResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.of(DeckResultCode.SHARE_DECK_PREVIEW_SUCCESS, response));
    }
}
