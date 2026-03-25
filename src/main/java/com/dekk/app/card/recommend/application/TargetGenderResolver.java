package com.dekk.app.card.recommend.application;

import com.dekk.app.card.domain.model.enums.TargetGender;
import com.dekk.app.user.domain.model.enums.Gender;
import java.util.List;

final class TargetGenderResolver {

    private TargetGenderResolver() {}

    static List<TargetGender> resolve(Gender gender) {
        if (gender == null) {
            return List.of(TargetGender.values());
        }
        return switch (gender) {
            case MALE -> List.of(TargetGender.MEN, TargetGender.OTHER);
            case FEMALE -> List.of(TargetGender.WOMEN, TargetGender.OTHER);
            case OTHER -> List.of(TargetGender.values());
        };
    }
}
