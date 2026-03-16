package com.dekk.deck.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.dekk.common.error.BusinessException;
import com.dekk.common.error.GlobalErrorCode;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class DeckCardCommandServiceConcurrencyTest {

    @Autowired
    private DeckCardCommandService deckCardCommandService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private DeckMemberRepository deckMemberRepository;

    @Autowired
    private DeckCardRepository deckCardRepository;

    // 💡 TransactionTemplate 의존성 주입 추가
    @Autowired
    private TransactionTemplate transactionTemplate;

    private Long userId = 1L;
    private Long customDeckId;
    private Long targetCardId = 100L;

    @BeforeEach
    void setUp() {
        Deck customDeck = Deck.createCustom(userId, "여름 코디 모음");
        deckRepository.save(customDeck);
        customDeckId = customDeck.getId();

        DeckMember hostMember = DeckMember.create(customDeckId, userId, DeckRole.HOST);
        deckMemberRepository.save(hostMember);
    }

    @AfterEach
    void tearDown() {
        transactionTemplate.executeWithoutResult(status -> {
            deckCardRepository.deleteAllByDeckId(customDeckId);
            deckMemberRepository.deleteAllByDeckId(customDeckId);
            deckRepository.findById(customDeckId).ifPresent(deckRepository::delete);
        });
    }

    @Test
    @DisplayName("동시에 100번의 동일한 카드 저장 요청이 오면, 분산 락을 통해 단 1장만 저장되고 나머지는 예외가 발생한다")
    void saveCardToCustomDeck_concurrency_test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger duplicateExceptionCount = new AtomicInteger();
        AtomicInteger lockExceptionCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    deckCardCommandService.saveToCustomDeck(userId, customDeckId, targetCardId);
                    successCount.incrementAndGet();
                } catch (DeckBusinessException e) {
                    if (e.errorCode() == DeckErrorCode.DECK_CARD_DUPLICATED) {
                        duplicateExceptionCount.incrementAndGet();
                    }
                } catch (BusinessException e) {
                    if (e.errorCode() == GlobalErrorCode.LOCK_ACQUISITION_FAILED) {
                        lockExceptionCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long savedCardCount = deckCardRepository.countByDeckId(customDeckId);
        assertThat(savedCardCount).isEqualTo(1L);

        assertThat(successCount.get()).isEqualTo(1);

        assertThat(duplicateExceptionCount.get() + lockExceptionCount.get()).isEqualTo(99);

        System.out.println("성공 횟수: " + successCount.get());
        System.out.println("중복 저장 방어(DECK_CARD_DUPLICATED) 횟수: " + duplicateExceptionCount.get());
        System.out.println("락 획득 실패 방어(LOCK_ACQUISITION_FAILED) 횟수: " + lockExceptionCount.get());
    }
}
