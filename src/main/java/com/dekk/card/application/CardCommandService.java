package com.dekk.card.application;

import com.dekk.card.application.dto.command.AssignCategoriesCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardCategory;
import com.dekk.card.domain.repository.CardCategoryRepository;
import com.dekk.card.domain.repository.CardRepository;
import com.dekk.category.application.CategoryQueryService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardCommandService {

    private final CardRepository cardRepository;
    private final CardCategoryRepository cardCategoryRepository;
    private final CategoryQueryService categoryQueryService;

    public void approveCard(Long cardId) {
        Card card = findCardOrThrow(cardId);
        card.approve();
    }

    public void rejectCard(Long cardId) {
        Card card = findCardOrThrow(cardId);
        card.reject();
    }

    public void assignCategories(Long cardId, AssignCategoriesCommand command) {
        validateCardExists(cardId);

        Set<Long> requestedIds = Set.copyOf(command.categoryIds());
        validateCategoryIds(requestedIds);

        Set<Long> originIds = getExistingCategoryIds(cardId);

        removeOldCategories(cardId, originIds, requestedIds);
        addNewCategories(cardId, originIds, requestedIds);
    }

    private Set<Long> getExistingCategoryIds(Long cardId) {
        return cardCategoryRepository.findAllByCardId(cardId).stream()
                .map(CardCategory::getCategoryId)
                .collect(Collectors.toSet());
    }

    private void removeOldCategories(Long cardId, Set<Long> originIds, Set<Long> requestedIds) {
        List<Long> categoryIdsToRemove =
                originIds.stream().filter(id -> !requestedIds.contains(id)).toList();
        cardCategoryRepository.softDeleteByCardIdAndCategoryIdIn(cardId, categoryIdsToRemove);
    }

    private void addNewCategories(Long cardId, Set<Long> originIds, Set<Long> requestedIds) {
        List<CardCategory> newCardCategories = requestedIds.stream()
                .filter(id -> !originIds.contains(id))
                .map(categoryId -> CardCategory.create(cardId, categoryId))
                .toList();
        cardCategoryRepository.saveAll(newCardCategories);
    }

    private void validateCategoryIds(Set<Long> categoryIds) {
        long count = categoryQueryService.countChildCategoryByIds(List.copyOf(categoryIds));
        if (count != categoryIds.size()) {
            throw new CardBusinessException(CardErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    private void validateCardExists(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new CardBusinessException(CardErrorCode.CARD_NOT_FOUND);
        }
    }

    private Card findCardOrThrow(Long cardId) {
        return cardRepository
                .findById(cardId)
                .orElseThrow(() -> new CardBusinessException(CardErrorCode.CARD_NOT_FOUND));
    }
}
