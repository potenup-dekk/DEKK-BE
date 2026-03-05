package com.dekk.category.domain.repository;

import com.dekk.category.domain.model.CardCategory;

import java.util.List;

public interface CardCategoryRepository {
    CardCategory save(CardCategory cardCategory);
    List<CardCategory> saveAll(List<CardCategory> cardCategories);
    boolean existsByCardIdAndCategoryId(Long cardId, Long categoryId);
    List<CardCategory> findAllByCategoryId(Long categoryId);
    List<CardCategory> findAllByCardId(Long cardId);
    void deleteAllByCategoryId(Long categoryId);
    void deleteAllByCardId(Long cardId);
}
