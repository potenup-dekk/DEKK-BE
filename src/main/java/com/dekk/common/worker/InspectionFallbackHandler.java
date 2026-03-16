package com.dekk.common.worker;

public interface InspectionFallbackHandler {
    void handleFailure(Long targetId);
}
