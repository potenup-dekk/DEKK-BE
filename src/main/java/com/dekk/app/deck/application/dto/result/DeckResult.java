package com.dekk.app.deck.application.dto.result;

import com.dekk.app.deck.domain.model.enums.DeckType;
import java.util.List;

public record DeckResult(Long deckId, String name, DeckType type, Long cardCount, List<String> previewImageUrls) {}
