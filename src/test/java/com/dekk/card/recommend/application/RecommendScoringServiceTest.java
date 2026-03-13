package com.dekk.card.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecommendScoringServiceTest {

    private final RecommendScoringService scoringService = new RecommendScoringService();

    @Nested
    @DisplayName("calculateBodyScore")
    class CalculateBodyScore {

        @ParameterizedTest(name = "{5}")
        @CsvSource({
                "175, 70, 175, 70, 1.0,    키와 몸무게가 완전히 일치하면 1.0을 반환한다",
                "175, 70, 180, 77, 0.0,    키와 몸무게 차이가 범위 한계와 정확히 같으면 0.0을 반환한다",
                "175, 70, 178, 74, 0.4143, 체형 차이에 따라 정확한 점수를 반환한다",
        })
        void shouldReturnExpectedScore(int uh, int uw, int ch, int cw, double expected, String description) {
            assertThat(scoringService.calculateBodyScore(uh, uw, ch, cw))
                    .isCloseTo(expected, within(0.001));
        }

        @Test
        @DisplayName("범위를 초과한 차이는 음수가 되지 않고 0.0으로 보정된다")
        void shouldClampTo0_whenDiffExceedsRange() {
            // height diff = 15 → 1 - 15/5 = -2.0  →  Math.max(0.0, -2.0) = 0.0
            // weight diff = 20 → 1 - 20/7 = -1.857.. →  Math.max(0.0, -1.857) = 0.0
            assertThat(scoringService.calculateBodyScore(175, 70, 190, 90))
                    .isGreaterThanOrEqualTo(0.0);
        }

        @Test
        @DisplayName("카드 체형 정보가 없으면 체형 제한 없는 카드로 간주해 1.0을 반환한다")
        void shouldReturn1_whenCardBodyInfoIsNull() {
            assertThat(scoringService.calculateBodyScore(175, 70, null, null))
                    .isCloseTo(1.0, within(0.001));
        }
    }

    @Nested
    @DisplayName("calculateCategoryPreferenceRatios")
    class CalculateCategoryPreferenceRatios {

        @Test
        @DisplayName("LIKE 이력이 없으면 빈 맵을 반환한다")
        void shouldReturnEmptyMap_whenNoLikedCategories() {
            Map<Long, Double> result = scoringService.calculateCategoryPreferenceRatios(List.of());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("카테고리가 하나뿐이면 해당 카테고리의 비율이 1.0이다")
        void shouldReturn1_whenSingleCategory() {
            Map<Long, Double> result = scoringService.calculateCategoryPreferenceRatios(List.of(1L, 1L, 1L));

            assertThat(result).hasSize(1);
            assertThat(result.get(1L)).isCloseTo(1.0, within(0.001));
        }

        @Test
        @DisplayName("모든 카테고리 비율의 합은 1.0이다")
        void shouldSumToOne_whenMultipleCategories() {
            // 카테고리 1: 3회, 카테고리 2: 1회 → 비율 합계 = 1.0
            List<Long> likedCategoryIds = List.of(1L, 1L, 1L, 2L);

            Map<Long, Double> result = scoringService.calculateCategoryPreferenceRatios(likedCategoryIds);

            double sum = result.values().stream().mapToDouble(Double::doubleValue).sum();
            assertThat(sum).isCloseTo(1.0, within(0.001));
        }

        @Test
        @DisplayName("등장 횟수에 비례하여 각 카테고리의 비율이 계산된다")
        void shouldCalculateRatioProportionally() {
            // 카테고리 1: 3회 / 4 = 0.75, 카테고리 2: 1회 / 4 = 0.25
            List<Long> likedCategoryIds = List.of(1L, 1L, 1L, 2L);

            Map<Long, Double> result = scoringService.calculateCategoryPreferenceRatios(likedCategoryIds);

            assertThat(result.get(1L)).isCloseTo(0.75, within(0.001));
            assertThat(result.get(2L)).isCloseTo(0.25, within(0.001));
        }

        @Test
        @DisplayName("모든 카테고리가 동일하게 등장하면 균등한 비율을 반환한다")
        void shouldReturnEqualRatios_whenAllCategoriesAppearOnce() {
            // 카테고리 1, 2, 3 각 1회 → 각 1/3 = 0.333..
            List<Long> likedCategoryIds = List.of(1L, 2L, 3L);

            Map<Long, Double> result = scoringService.calculateCategoryPreferenceRatios(likedCategoryIds);

            assertThat(result.get(1L)).isCloseTo(1.0 / 3, within(0.001));
            assertThat(result.get(2L)).isCloseTo(1.0 / 3, within(0.001));
            assertThat(result.get(3L)).isCloseTo(1.0 / 3, within(0.001));
        }
    }

    @Nested
    @DisplayName("calculateCategoryScore")
    class CalculateCategoryScore {

        @Test
        @DisplayName("카드 카테고리가 없으면 0.0을 반환한다")
        void shouldReturn0_whenCardHasNoCategories() {
            Map<Long, Double> preferences = Map.of(1L, 0.6, 2L, 0.4);

            double result = scoringService.calculateCategoryScore(List.of(), preferences);

            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("유저 LIKE 이력이 없으면(빈 맵) 0.0을 반환한다")
        void shouldReturn0_whenPreferencesIsEmpty() {
            double result = scoringService.calculateCategoryScore(List.of(1L, 2L), Map.of());

            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("카드 카테고리가 유저 선호 맵에 모두 존재하면 선호 비율의 평균을 반환한다")
        void shouldReturnAverageOfPreferences_whenAllCategoriesMatched() {
            // 카테고리 1 → 0.6, 카테고리 2 → 0.4 → 평균 = 0.5
            Map<Long, Double> preferences = Map.of(1L, 0.6, 2L, 0.4);

            double result = scoringService.calculateCategoryScore(List.of(1L, 2L), preferences);

            assertThat(result).isCloseTo(0.5, within(0.001));
        }

        @Test
        @DisplayName("카드 카테고리 일부가 유저 선호 맵에 없으면 매칭된 카테고리만 평균을 낸다")
        void shouldAverageOnlyMatchedCategories() {
            Map<Long, Double> preferences = Map.of(1L, 0.6, 2L, 0.4);

            double result = scoringService.calculateCategoryScore(List.of(1L, 3L), preferences);

            assertThat(result).isCloseTo(0.6, within(0.001));
        }

        @Test
        @DisplayName("카드 카테고리가 유저 선호 맵에 전혀 없으면 0.0을 반환한다")
        void shouldReturn0_whenNoCategoryMatched() {
            Map<Long, Double> preferences = Map.of(1L, 0.6, 2L, 0.4);

            double result = scoringService.calculateCategoryScore(List.of(3L, 4L), preferences);

            assertThat(result).isEqualTo(0.0);
        }
    }
}
