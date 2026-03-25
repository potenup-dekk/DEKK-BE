package com.dekk.app.user.domain.repository;

import com.dekk.app.user.domain.model.Profile;

public interface ProfileRepository {
    Profile save(Profile profile);

    boolean existsByNickname(String nickname);
}
