package com.dekk.deck.application.dto.result;

import java.util.List;

public record GuestSharedDeckCardResult(
        Long cardId, String cardImageUrl, Integer height, Integer weight, List<String> tags) {}
