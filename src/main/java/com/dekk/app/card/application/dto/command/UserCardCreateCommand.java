package com.dekk.app.card.application.dto.command;

import java.util.List;

public record UserCardCreateCommand(
        Long userId, CardImageCreateCommand cardImage, List<ProductCreateCommand> productCreateCommands, String tags) {}
