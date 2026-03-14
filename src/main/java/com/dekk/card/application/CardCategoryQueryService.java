package com.dekk.card.application;

import com.dekk.card.domain.model.CardCategory;
import com.dekk.card.domain.repository.CardCategoryRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardCategoryQueryService {

    private final CardCategoryRepository cardCategoryRepository;

    public Map<Long, List<Long>> getCardCategoryMap(List<Long> cardIds) {
        return cardCategoryRepository.findByCardIdIn(cardIds).stream()
                .collect(Collectors.groupingBy(
                        CardCategory::getCardId, Collectors.mapping(CardCategory::getCategoryId, Collectors.toList())));
    }
}
