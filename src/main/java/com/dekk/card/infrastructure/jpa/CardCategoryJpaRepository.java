package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.CardCategory;
import com.dekk.card.domain.repository.CardCategoryProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardCategoryJpaRepository extends JpaRepository<CardCategory, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE CardCategory cc SET cc.deletedAt = CURRENT_TIMESTAMP WHERE cc.categoryId = :categoryId AND cc.deletedAt IS NULL")
    void softDeleteAllByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE CardCategory cc SET cc.deletedAt = CURRENT_TIMESTAMP WHERE cc.categoryId IN :categoryIds AND cc.deletedAt IS NULL")
    void softDeleteAllByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE CardCategory cc SET cc.deletedAt = CURRENT_TIMESTAMP WHERE cc.cardId = :cardId AND cc.categoryId IN :categoryIds AND cc.deletedAt IS NULL")
    void softDeleteByCardIdAndCategoryIdIn(@Param("cardId") Long cardId, @Param("categoryIds") List<Long> categoryIds);

    List<CardCategory> findAllByCardId(Long cardId);

    @Query(
            "SELECT new com.dekk.card.domain.repository.CardCategoryProjection(cc.cardId, cc.categoryId) FROM CardCategory cc WHERE cc.cardId IN :cardIds")
    List<CardCategoryProjection> findCardCategoryProjectionsByCardIdIn(@Param("cardIds") List<Long> cardIds);

    @Query("SELECT cc.categoryId FROM CardCategory cc WHERE cc.cardId = :cardId")
    List<Long> findCategoryIdsByCardId(@Param("cardId") Long cardId);
}
