package com.dekk.app.user.application.command;

import com.dekk.app.user.domain.model.enums.Gender;

public record UserOnboardingCommand(String nickname, Integer height, Integer weight, Gender gender) {}
