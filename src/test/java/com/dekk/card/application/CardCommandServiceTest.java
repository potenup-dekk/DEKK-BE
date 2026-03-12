package com.dekk.card.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

import com.dekk.card.application.dto.command.AssignCategoriesCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardCategory;
import com.dekk.card.domain.repository.CardCategoryRepository;
import com.dekk.card.domain.repository.CardRepository;
import com.dekk.category.application.CategoryQueryService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardCommandServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardCategoryRepository cardCategoryRepository;

    @Mock
    private CategoryQueryService categoryQueryService;

    @InjectMocks
    private CardCommandService cardCommandService;

    private static final Long CARD_ID = 1L;

    @Nested
    @DisplayName("카드 카테고리 지정")
    class AssignCategories {

        @Test
        @DisplayName("기존 카테고리가 없을 때 요청한 카테고리가 모두 새로 추가된다")
        void addAllWhenNoPreviousCategories() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(mockCard()));
            given(categoryQueryService.countChildCategoryByIds(anyList())).willReturn(3L);
            given(cardCategoryRepository.findAllByCardId(CARD_ID)).willReturn(List.of());

            AssignCategoriesCommand command = new AssignCategoriesCommand(List.of(1L, 2L, 3L));

            // when
            cardCommandService.assignCategories(CARD_ID, command);

            // then
            verify(cardCategoryRepository).saveAll(anyList());
            verify(cardCategoryRepository).softDeleteByCardIdAndCategoryIdIn(eq(CARD_ID), eq(List.of()));
        }

        @Test
        @DisplayName("요청한 카테고리가 비어있으면 기존 카테고리가 모두 삭제된다")
        void removeAllWhenEmptyRequest() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(mockCard()));
            given(categoryQueryService.countChildCategoryByIds(anyList())).willReturn(0L);
            given(cardCategoryRepository.findAllByCardId(CARD_ID))
                    .willReturn(List.of(
                            CardCategory.create(CARD_ID, 1L),
                            CardCategory.create(CARD_ID, 2L)));

            AssignCategoriesCommand command = new AssignCategoriesCommand(List.of());

            // when
            cardCommandService.assignCategories(CARD_ID, command);

            // then
            verify(cardCategoryRepository).softDeleteByCardIdAndCategoryIdIn(eq(CARD_ID), anyList());
            verify(cardCategoryRepository).saveAll(List.of());
        }

        @Test
        @DisplayName("기존과 동일한 카테고리를 요청하면 추가/삭제가 발생하지 않는다")
        void noChangesWhenSameCategories() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(mockCard()));
            given(categoryQueryService.countChildCategoryByIds(anyList())).willReturn(2L);
            given(cardCategoryRepository.findAllByCardId(CARD_ID))
                    .willReturn(List.of(
                            CardCategory.create(CARD_ID, 1L),
                            CardCategory.create(CARD_ID, 2L)));

            AssignCategoriesCommand command = new AssignCategoriesCommand(List.of(1L, 2L));

            // when
            cardCommandService.assignCategories(CARD_ID, command);

            // then
            verify(cardCategoryRepository).softDeleteByCardIdAndCategoryIdIn(eq(CARD_ID), eq(List.of()));
            verify(cardCategoryRepository).saveAll(List.of());
        }

        @Test
        @DisplayName("일부 카테고리가 추가되고 일부가 삭제된다")
        void addAndRemoveCategories() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(mockCard()));
            given(categoryQueryService.countChildCategoryByIds(anyList())).willReturn(2L);
            given(cardCategoryRepository.findAllByCardId(CARD_ID))
                    .willReturn(List.of(
                            CardCategory.create(CARD_ID, 1L),
                            CardCategory.create(CARD_ID, 2L)));

            // 기존: [1, 2] → 요청: [2, 3] → 삭제: [1], 추가: [3]
            AssignCategoriesCommand command = new AssignCategoriesCommand(List.of(2L, 3L));

            // when
            cardCommandService.assignCategories(CARD_ID, command);

            // then
            verify(cardCategoryRepository).softDeleteByCardIdAndCategoryIdIn(eq(CARD_ID), anyList());
            verify(cardCategoryRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("존재하지 않는 카드에 카테고리를 지정하면 예외가 발생한다")
        void throwsExceptionWhenCardNotFound() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.empty());
            AssignCategoriesCommand command = new AssignCategoriesCommand(List.of(1L));

            // when & then
            assertThatThrownBy(() -> cardCommandService.assignCategories(CARD_ID, command))
                    .isInstanceOf(CardBusinessException.class);
        }
    }

    @Nested
    @DisplayName("카드 승인")
    class ApproveCard {

        @Test
        @DisplayName("카드를 승인한다")
        void approveCard() {
            // given
            Card card = mockCard();
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(card));

            // when
            cardCommandService.approveCard(CARD_ID);

            // then
            verify(cardRepository).findById(CARD_ID);
        }

        @Test
        @DisplayName("존재하지 않는 카드를 승인하면 예외가 발생한다")
        void throwsExceptionWhenCardNotFound() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> cardCommandService.approveCard(CARD_ID))
                    .isInstanceOf(CardBusinessException.class);
        }
    }

    @Nested
    @DisplayName("카드 반려")
    class RejectCard {

        @Test
        @DisplayName("카드를 반려한다")
        void rejectCard() {
            // given
            Card card = mockCard();
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.of(card));

            // when
            cardCommandService.rejectCard(CARD_ID);

            // then
            verify(cardRepository).findById(CARD_ID);
        }

        @Test
        @DisplayName("존재하지 않는 카드를 반려하면 예외가 발생한다")
        void throwsExceptionWhenCardNotFound() {
            // given
            given(cardRepository.findById(CARD_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> cardCommandService.rejectCard(CARD_ID))
                    .isInstanceOf(CardBusinessException.class);
        }
    }

    private Card mockCard() {
        return org.mockito.Mockito.mock(Card.class);
    }
}
