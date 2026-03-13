package com.dekk.card.application.dto.command;

import java.util.List;

public record AssignCategoriesCommand(List<Long> categoryIds) {}
