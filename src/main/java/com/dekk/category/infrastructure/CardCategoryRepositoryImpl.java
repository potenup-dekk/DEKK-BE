package com.dekk.category.infrastructure;

import com.dekk.category.domain.repository.CardCategoryRepository;
import com.dekk.category.infrastructure.jpa.CardCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardCategoryRepositoryImpl implements CardCategoryRepository {

    private final CardCategoryJpaRepository cardCategoryJpaRepository;

    @Override
    public void softDeleteAllByCategoryId(Long categoryId) {
        cardCategoryJpaRepository.softDeleteAllByCategoryId(categoryId);
    }

    @Override
    public void softDeleteAllByCategoryIdIn(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return;
        }
        cardCategoryJpaRepository.softDeleteAllByCategoryIdIn(categoryIds);
    }
}
