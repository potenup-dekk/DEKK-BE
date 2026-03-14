package com.dekk.card.application;

import com.dekk.card.domain.repository.CardCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardCategoryQueryService {

    private final CardCategoryRepository cardCategoryRepository;

    public List<Long> getCategoryIdsByCardIds(List<Long> cardIds) {
        return cardCategoryRepository.findCategoryIdsByCardIds(cardIds);
    }

    public List<Long> getCategoryIdsByCardId(Long cardId) {
        return cardCategoryRepository.findCategoryIdsByCardId(cardId);
    }
}
