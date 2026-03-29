package com.dekk.global.security.oauth2.dto;

import com.dekk.app.user.domain.model.enums.Provider;
import java.util.Map;

public interface OAuth2UserInfo {
    Provider getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    Map<String, Object> getAttributes();
}
