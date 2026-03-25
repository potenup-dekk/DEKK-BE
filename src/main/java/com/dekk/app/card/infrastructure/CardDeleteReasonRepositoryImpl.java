package com.dekk.app.card.infrastructure;

import com.dekk.app.card.domain.model.CardDeleteReason;
import com.dekk.app.card.domain.repository.CardDeleteReasonRepository;
import com.dekk.app.card.infrastructure.jpa.CardDeleteReasonJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardDeleteReasonRepositoryImpl implements CardDeleteReasonRepository {

    private final CardDeleteReasonJpaRepository cardDeleteReasonJpaRepository;

    @Override
    public CardDeleteReason save(CardDeleteReason cardDeleteReason) {
        return cardDeleteReasonJpaRepository.save(cardDeleteReason);
    }
}
