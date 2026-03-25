package com.dekk.app.card.application.dto.command;

import java.util.List;

public record AssignCategoriesCommand(List<Long> categoryIds) {}
