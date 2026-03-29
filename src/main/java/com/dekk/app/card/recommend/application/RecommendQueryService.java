package com.dekk.app.card.recommend.application;

import com.dekk.app.activelog.application.ActiveLogQueryService;
import com.dekk.app.activelog.domain.model.SwipeType;
import com.dekk.app.card.application.CardCategoryQueryService;
import com.dekk.app.card.application.CardQueryService;
import com.dekk.app.card.application.dto.query.RecommendCandidateQuery;
import com.dekk.app.card.application.dto.result.MemberCardResult;
import com.dekk.app.card.recommend.application.dto.RecommendCardResult;
import com.dekk.app.user.application.UserQueryService;
import com.dekk.app.user.application.dto.result.UserInfoResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendQueryService {

    private static final double RECOMMEND_RATIO = 0.7;

    private final CardQueryService cardQueryService;
    private final UserQueryService userQueryService;
    private final ActiveLogQueryService activeLogQueryService;
    private final CardCategoryQueryService cardCategoryQueryService;
    private final RecommendScoringService recommendScoringService;

    public Page<RecommendCardResult> getRecommendCards(Long userId, Pageable pageable) {
        int totalNeeded = (int) (pageable.getOffset() + pageable.getPageSize());
        int recommendCount = (int) Math.ceil(totalNeeded * RECOMMEND_RATIO);

        Set<Long> swipedIds = activeLogQueryService.getAllSwipedCardIds(userId);

        List<MemberCardResult> recommendCards =
                rankCandidates(userId, swipedIds).stream().limit(recommendCount).toList();

        Set<Long> excludeForNormal = new HashSet<>(swipedIds);
        recommendCards.forEach(c -> excludeForNormal.add(c.cardId()));

        int normalCount = totalNeeded - recommendCards.size();
        List<MemberCardResult> normalCards = cardQueryService.getLatestCards(excludeForNormal, normalCount);

        List<RecommendCardResult> allResults = new ArrayList<>(recommendCards.size() + normalCards.size());
        recommendCards.forEach(c -> allResults.add(RecommendCardResult.recommended(c)));
        normalCards.forEach(c -> allResults.add(RecommendCardResult.normal(c)));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());
        List<RecommendCardResult> pageContent = start >= allResults.size() ? List.of() : allResults.subList(start, end);

        return new PageImpl<>(pageContent, pageable, allResults.size());
    }

    private List<MemberCardResult> rankCandidates(Long userId, Set<Long> swipedIds) {
        UserInfoResult userInfo = userQueryService.getMyInfo(userId);

        List<MemberCardResult> candidates = fetchCandidates(userInfo).stream()
                .filter(card -> !swipedIds.contains(card.cardId()))
                .toList();

        List<Long> likedCategoryIds = getLikedCategoryIds(userId);
        Map<Long, Double> preferences = recommendScoringService.calculateCategoryPreferenceRatios(likedCategoryIds);

        List<Long> candidateCardIds =
                candidates.stream().map(MemberCardResult::cardId).toList();
        Map<Long, List<Long>> cardCategoryMap = cardCategoryQueryService.getCardCategoryMap(candidateCardIds);

        return recommendScoringService.rank(
                userInfo.height(), userInfo.weight(), candidates, cardCategoryMap, preferences);
    }

    private List<MemberCardResult> fetchCandidates(UserInfoResult userInfo) {
        return cardQueryService
                .getRecommendCandidates(RecommendCandidateQuery.of(
                        TargetGenderResolver.resolve(userInfo.gender()), userInfo.height(), userInfo.weight()))
                .stream()
                .map(MemberCardResult::from)
                .toList();
    }

    private List<Long> getLikedCategoryIds(Long userId) {
        List<Long> likedCardIds = activeLogQueryService.getSwipedCardIds(userId, SwipeType.LIKE);
        return cardCategoryQueryService.getCardCategoryMap(likedCardIds).values().stream()
                .flatMap(List::stream)
                .toList();
    }
}
