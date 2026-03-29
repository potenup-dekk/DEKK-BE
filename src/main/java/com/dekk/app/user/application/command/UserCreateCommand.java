package com.dekk.app.user.application.command;

import com.dekk.app.user.domain.model.enums.Provider;

public record UserCreateCommand(String email, Provider provider, String providerId) {}
