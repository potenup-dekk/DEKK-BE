package com.dekk.app.activelog.application;

import com.dekk.app.activelog.application.dto.command.SwipeCommand;
import com.dekk.app.activelog.domain.model.ActiveLog;
import com.dekk.app.activelog.domain.model.SwipeType;
import com.dekk.app.activelog.domain.repository.ActiveLogRepository;
import com.dekk.app.deck.application.DeckCardCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActiveLogCommandService {

    private final ActiveLogRepository activeLogRepository;
    private final DeckCardCommandService deckCardCommandService;

    public void saveSwipeAction(SwipeCommand command) {
        if (activeLogRepository.existsByUserIdAndCardId(command.userId(), command.cardId())) {
            return;
        }

        ActiveLog activeLog = ActiveLog.create(command.userId(), command.cardId(), command.swipeType());
        activeLogRepository.save(activeLog);

        if (command.swipeType() == SwipeType.LIKE) {
            deckCardCommandService.saveToDefaultDeck(command.userId(), command.cardId());
        }
    }
}
