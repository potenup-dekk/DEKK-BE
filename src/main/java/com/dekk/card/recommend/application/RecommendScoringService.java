package com.dekk.card.recommend.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RecommendScoringService {

    private static final double HEIGHT_RANGE = 5.0;
    private static final double WEIGHT_RANGE = 7.0;
    private static final double MAX_SCORE = 1.0;
    private static final double MIN_SCORE = 0.0;
    private static final double SCORE_DIMENSION_COUNT = 2.0;

    public double calculateBodyScore(int userHeight, int userWeight, Integer cardHeight, Integer cardWeight) {
        double heightScore = calculateDimensionScore(userHeight, cardHeight, HEIGHT_RANGE);
        double weightScore = calculateDimensionScore(userWeight, cardWeight, WEIGHT_RANGE);
        return (heightScore + weightScore) / SCORE_DIMENSION_COUNT;
    }

    private double calculateDimensionScore(int userValue, Integer cardValue, double range) {
        if (cardValue == null) {
            return MAX_SCORE;
        }
        double diff = Math.abs(userValue - cardValue);
        double rawScore = MAX_SCORE - diff / range;

        return Math.max(MIN_SCORE, rawScore);
    }

    public Map<Long, Double> calculateCategoryPreferenceRatios(List<Long> likedCategoryIds) {
        if (likedCategoryIds.isEmpty()) {
            return Map.of();
        }

        long total = likedCategoryIds.size();
        return likedCategoryIds.stream()
                .collect(groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> (double) e.getValue() / total));
    }

    public double calculateCategoryScore(List<Long> cardCategoryIds, Map<Long, Double> preferences) {
        if (cardCategoryIds.isEmpty() || preferences.isEmpty()) {
            return MIN_SCORE;
        }

        return cardCategoryIds.stream()
                .filter(preferences::containsKey)
                .mapToDouble(preferences::get)
                .average()
                .orElse(MIN_SCORE);
    }
}
