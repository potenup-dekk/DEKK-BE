package com.dekk.deck.application;

import com.dekk.common.lock.DistributedLock;
import com.dekk.deck.application.dto.result.ShareTokenResult;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckMember;
import com.dekk.deck.domain.model.enums.DeckRole;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckMemberRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.redis.DeckInviteRedisRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShareDeckCommandService {

    private static final Duration TOKEN_TTL = Duration.ofHours(24);
    private static final long OVERLAP_THRESHOLD_SECONDS = 600L;
    private static final int MAX_TOTAL_DECK_COUNT = 9;
    private static final int MAX_GUEST_COUNT = 5;

    private final DeckRepository deckRepository;
    private final DeckMemberRepository deckMemberRepository;
    private final DeckInviteRedisRepository deckInviteRedisRepository;
    private final DeckCardRepository deckCardRepository;

    @Transactional
    public ShareTokenResult turnOnShareAndGetToken(Long userId, Long deckId) {
        Deck deck = getDeckAsHost(deckId, userId);

        validateNotDefaultDeck(deck);

        if (deck.isCustom()) {
            deck.changeToShared();
        }

        return getOrCreateShareToken(deckId);
    }

    @Transactional
    public void turnOffShare(Long userId, Long deckId) {
        Deck deck = getDeckAsHost(deckId, userId);

        validateNotDefaultDeck(deck);

        if (deck.isShared()) {
            deck.changeToCustom();
        }

        deckMemberRepository.deleteAllGuestsByDeckId(deckId, DeckRole.GUEST);
        clearShareToken(deckId);
    }

    @DistributedLock(key = "'join_deck:' + #userId")
    public void joinSharedDeck(Long userId, String token) {
        Long deckId = deckInviteRedisRepository
                .getDeckIdByToken(token)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.SHARE_TOKEN_EXPIRED));

        Deck deck = getDeckOrThrow(deckId);

        validateSharedDeck(deck);
        validateNotAlreadyJoined(deckId, userId);
        validateDeckLimit(userId);
        validateGuestLimit(deckId);

        deckMemberRepository.reactivateOrSave(deckId, userId, DeckRole.GUEST);
    }

    @Transactional
    public void leaveSharedDeck(Long userId, Long deckId) {
        DeckMember member = getDeckMemberOrThrow(deckId, userId);

        validateNotHost(member);

        deckMemberRepository.delete(member);
    }

    @Transactional
    public void handleHostWithdrawal(Long deckId, Long hostUserId) {
        DeckMember hostMember = getDeckMemberOrThrow(deckId, hostUserId);

        if (!hostMember.isHost()) {
            return;
        }

        Optional<DeckMember> oldestGuest =
                deckMemberRepository.findFirstByDeckIdAndRoleOrderByCreatedAtAsc(deckId, DeckRole.GUEST);

        oldestGuest.ifPresentOrElse(guest -> succeedHost(guest, hostMember), () -> deleteSharedDeck(deckId));
    }

    private void succeedHost(DeckMember guest, DeckMember hostMember) {
        guest.promoteToHost();
        deckMemberRepository.delete(hostMember);
    }

    private Deck getDeckAsHost(Long deckId, Long userId) {
        DeckMember member = getDeckMemberOrThrow(deckId, userId);

        validateHostRole(member);

        return getDeckOrThrow(deckId);
    }

    private ShareTokenResult getOrCreateShareToken(Long deckId) {
        Optional<String> existingToken = deckInviteRedisRepository.getTokenByDeckId(deckId);

        if (existingToken.isPresent()) {
            long remainingSeconds = deckInviteRedisRepository.getRemainingSeconds(deckId);

            if (remainingSeconds > OVERLAP_THRESHOLD_SECONDS) {
                return new ShareTokenResult(existingToken.get(), remainingSeconds);
            }
        }

        String newToken = UUID.randomUUID().toString().replace("-", "");
        deckInviteRedisRepository.saveToken(deckId, newToken, TOKEN_TTL);

        return new ShareTokenResult(newToken, TOKEN_TTL.getSeconds());
    }

    private void clearShareToken(Long deckId) {
        String currentToken = deckInviteRedisRepository.getTokenByDeckId(deckId).orElse(null);
        deckInviteRedisRepository.deleteTokens(deckId, currentToken);
    }

    private void validateSharedDeck(Deck deck) {
        if (!deck.isShared()) {
            throw new DeckBusinessException(DeckErrorCode.DECK_IS_NOT_SHARED);
        }
    }

    private void validateNotAlreadyJoined(Long deckId, Long userId) {
        if (deckMemberRepository.findByDeckIdAndUserId(deckId, userId).isPresent()) {
            throw new DeckBusinessException(DeckErrorCode.ALREADY_JOINED_DECK);
        }
    }

    private void validateDeckLimit(Long userId) {
        long currentDeckCount = deckMemberRepository.countByUserId(userId);
        if (currentDeckCount >= MAX_TOTAL_DECK_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.DECK_LIMIT_EXCEEDED);
        }
    }

    private void validateGuestLimit(Long deckId) {
        long guestCount = deckMemberRepository.countByDeckIdAndRole(deckId, DeckRole.GUEST);
        if (guestCount >= MAX_GUEST_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.DECK_GUEST_LIMIT_EXCEEDED);
        }
    }

    private void validateHostRole(DeckMember member) {
        if (!member.isHost()) {
            throw new DeckBusinessException(DeckErrorCode.GUEST_CANNOT_GENERATE_TOKEN);
        }
    }

    private DeckMember getDeckMemberOrThrow(Long deckId, Long userId) {
        return deckMemberRepository
                .findByDeckIdAndUserId(deckId, userId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DECK_MEMBER_NOT_FOUND));
    }

    private Deck getDeckOrThrow(Long deckId) {
        return deckRepository
                .findById(deckId)
                .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));
    }

    private void deleteSharedDeck(Long deckId) {
        Deck deck = getDeckOrThrow(deckId);

        deckCardRepository.deleteAllByDeckId(deckId);
        deckMemberRepository.deleteAllByDeckId(deckId);
        deckRepository.delete(deck);
        clearShareToken(deckId);
    }

    private void validateNotDefaultDeck(Deck deck) {
        if (deck.isDefault()) {
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED);
        }
    }

    private void validateNotHost(DeckMember member) {
        if (member.isHost()) {
            throw new DeckBusinessException(DeckErrorCode.HOST_CANNOT_LEAVE_DECK);
        }
    }
}
