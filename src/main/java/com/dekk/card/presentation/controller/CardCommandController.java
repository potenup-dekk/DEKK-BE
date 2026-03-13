package com.dekk.card.presentation.controller;

import com.dekk.card.application.CardCommandService;
import com.dekk.card.presentation.request.AssignCategoriesRequest;
import com.dekk.card.presentation.response.CardResultCode;
import com.dekk.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/cards")
@RequiredArgsConstructor
public class CardCommandController implements CardCommandApi {

    private final CardCommandService cardCommandService;

    @Override
    @PatchMapping("/{cardId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveCard(@PathVariable("cardId") Long cardId) {
        cardCommandService.approveCard(cardId);
        return ResponseEntity.ok(ApiResponse.from(CardResultCode.CARD_APPROVE_SUCCESS));
    }

    @Override
    @PatchMapping("/{cardId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectCard(@PathVariable("cardId") Long cardId) {
        cardCommandService.rejectCard(cardId);
        return ResponseEntity.ok(ApiResponse.from(CardResultCode.CARD_REJECT_SUCCESS));
    }

    @Override
    @PutMapping("/{cardId}/categories")
    public ResponseEntity<ApiResponse<Void>> assignCategories(
            @PathVariable("cardId") Long cardId, @Valid @RequestBody AssignCategoriesRequest request) {
        cardCommandService.assignCategories(cardId, request.toCommand());
        return ResponseEntity.ok(ApiResponse.from(CardResultCode.CARD_CATEGORIES_ASSIGNED));
    }
}
