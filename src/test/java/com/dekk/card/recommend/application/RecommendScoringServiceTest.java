package com.dekk.card.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.user.application.dto.result.UserInfoResult;
import com.dekk.user.domain.model.enums.Gender;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecommendScoringServiceTest {

    private final RecommendScoringService scoringService = new RecommendScoringService();

    @Nested
    @DisplayName("체형 차이에 따른 점수 계산")
    class BodyScoreCalculation {

        @Test
        @DisplayName("키와 몸무게가 완전히 일치하면 1.0을 반환한다")
        void shouldReturn1_whenPerfectMatch() {
            UserInfoResult user = userInfo(175, 70);
            MemberCardResult card = card(175, 70);

            assertThat(scoringService.calculateBodyScore(user, card)).isCloseTo(1.0, within(0.001));
        }

        @Test
        @DisplayName("키와 몸무게 차이가 범위 한계와 정확히 같으면 0.0을 반환한다")
        void shouldReturn0_whenDiffEqualsRange() {
            // height diff = 5 → 1 - 5/5 = 0.0  (음수 보정 없이 수식 자체가 0.0)
            // weight diff = 7 → 1 - 7/7 = 0.0
            UserInfoResult user = userInfo(175, 70);
            MemberCardResult card = card(180, 77);

            assertThat(scoringService.calculateBodyScore(user, card)).isCloseTo(0.0, within(0.001));
        }

        @Test
        @DisplayName("체형 차이에 따라 정확한 점수를 반환한다")
        void shouldReturnExactScore_whenDiffIsPartialRange() {
            // height diff = 3 → 1 - 3/5 = 0.4
            // weight diff = 4 → 1 - 4/7 = 0.4286..
            // bodyScore = (0.4 + 0.4286) / 2 = 0.4143..
            UserInfoResult user = userInfo(175, 70);
            MemberCardResult card = card(178, 74);

            assertThat(scoringService.calculateBodyScore(user, card)).isCloseTo(0.4143, within(0.001));
        }

        @Test
        @DisplayName("범위를 초과한 차이는 음수가 되지 않고 0.0으로 보정된다")
        void shouldClampTo0_whenDiffExceedsRange() {
            // height diff = 15 → 1 - 15/5 = -2.0  →  Math.max(0.0, -2.0) = 0.0
            // weight diff = 20 → 1 - 20/7 = -1.857.. →  Math.max(0.0, -1.857) = 0.0
            UserInfoResult user = userInfo(175, 70);
            MemberCardResult card = card(190, 90);

            assertThat(scoringService.calculateBodyScore(user, card)).isGreaterThanOrEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("카드 체형 정보 없음")
    class CardValueNull {

        @Test
        @DisplayName("카드 체형 정보가 없으면 체형 제한 없는 카드로 간주해 1.0을 반환한다")
        void shouldReturn1_whenCardBodyInfoIsNull() {
            UserInfoResult user = userInfo(175, 70);
            MemberCardResult card = card(null, null);

            assertThat(scoringService.calculateBodyScore(user, card)).isCloseTo(1.0, within(0.001));
        }
    }

    private UserInfoResult userInfo(Integer height, Integer weight) {
        return new UserInfoResult(1L, "test@test.com", "닉네임", height, weight, Gender.MALE, "ACTIVE", "USER");
    }

    private MemberCardResult card(Integer height, Integer weight) {
        return new MemberCardResult(1L, "http://image.url/1", height, weight, List.of(), List.of());
    }
}
