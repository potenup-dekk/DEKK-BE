package com.dekk.card.presentation.controller;

import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.query.AdminCardSearchQuery;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.presentation.dto.response.AdminCardResponse;
import com.dekk.card.presentation.response.CardResultCode;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/cards")
@RequiredArgsConstructor
public class AdminCardQueryController implements AdminCardQueryApi {

    private final CardQueryService cardQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AdminCardResponse>>> getAdminCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "DESC") String sort,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false) String originId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        AdminCardSearchQuery condition = new AdminCardSearchQuery(
                cardId,
                originId,
                status,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(LocalTime.MAX) : null);

        PageResponse<AdminCardResponse> response = PageResponse.from(
                cardQueryService.searchCardsForAdmin(condition, pageable).map(AdminCardResponse::from));

        return ResponseEntity.ok(ApiResponse.of(CardResultCode.ADMIN_CARD_LIST_SUCCESS, response));
    }
}
