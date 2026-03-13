package com.dekk.card.domain.repository;

import com.dekk.card.domain.model.CardCategory;
import java.util.List;

public interface CardCategoryRepository {
    void softDeleteAllByCategoryId(Long categoryId);

    void softDeleteAllByCategoryIdIn(List<Long> categoryIds);

    void softDeleteByCardIdAndCategoryIdIn(Long cardId, List<Long> categoryIds);

    List<CardCategory> findAllByCardId(Long cardId);

    List<CardCategory> saveAll(List<CardCategory> cardCategories);
}
