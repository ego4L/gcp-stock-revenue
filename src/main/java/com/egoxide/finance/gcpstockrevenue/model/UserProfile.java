package com.egoxide.finance.gcpstockrevenue.model;

import com.google.cloud.firestore.annotation.DocumentId;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record UserProfile(
        @DocumentId
        String uid,
        String email,
        String name,
        Instant createdAt,
        List<String> roles) {
    public UserProfile {
        if (roles == null) {
            roles = Collections.emptyList();
        }
    }

    public boolean isAdmin() {
        return this.roles.contains("ADMIN");
    }
}
