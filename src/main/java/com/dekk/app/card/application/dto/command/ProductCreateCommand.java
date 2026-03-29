package com.dekk.app.card.application.dto.command;

public record ProductCreateCommand(
        ProductImageCreateCommand productImage,
        String brand,
        String name,
        Integer price,
        String originId,
        String option,
        boolean isSimilar,
        String productUrl,
        boolean isActive) {}
