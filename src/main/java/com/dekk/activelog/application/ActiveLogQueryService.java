package com.dekk.activelog.application;

import com.dekk.activelog.domain.model.SwipeType;
import com.dekk.activelog.domain.repository.ActiveLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveLogQueryService {

    private final ActiveLogRepository activeLogRepository;

    /**
     * [추천 필터링용] 이미 스와이프한 모든 카드 ID 조회
     * 목적: LIKE와 DISLIKE 전체를 추천 후보에서 제외하기 위함
     * 성능: 추천 서버에서 contains() 체크 시 O(1) 성능을 위해 Set으로 반환
     */
    public Set<Long> getAllSwipedCardIds(Long userId) {
        List<SwipeType> targetTypes = List.of(SwipeType.LIKE, SwipeType.DISLIKE);
        List<Long> cardIds = activeLogRepository.findCardIdsByUserIdAndSwipeTypes(userId, targetTypes);

        return new HashSet<>(cardIds);
    }

    /**
     * [선호도 분석용] 특정 반응(예: LIKE)을 보인 카드 ID 목록 조회
     * 목적: 사용자가 좋아한 카드의 카데고리 가중치를 계산하기 위함
     */
    public List<Long> getSwipedCardIds(Long userId, SwipeType swipeType) {
        return activeLogRepository.findCardIdsByUserIdAndSwipeType(userId, swipeType);
    }
}
