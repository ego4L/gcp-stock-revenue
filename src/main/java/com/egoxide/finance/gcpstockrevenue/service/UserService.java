package com.egoxide.finance.gcpstockrevenue.service;

import com.egoxide.finance.gcpstockrevenue.model.UserProfile;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Create or update user profile in Firestore.
     * @param uid Unique user ID from Firebase JWT
     * @param email User email  (could be taken from JWT)
     * @param name User name
     */
    public UserProfile createOrUpdateUserProfile(String uid, String email, String name)
            throws ExecutionException, InterruptedException {

        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(uid);

        UserProfile existingProfile = documentReference.get().get().toObject(UserProfile.class);

        if (existingProfile == null) {
            UserProfile newProfile = new UserProfile(uid, email, name, Instant.now(), List.of("USER"));
            WriteResult result = documentReference.set(newProfile).get();
            log.info("New user created at: {}", result.getUpdateTime());

            return newProfile;
        } else {
            log.info("User profile already exists for UID: {}", uid);
            return existingProfile;
        }
    }

    public UserProfile getUserProfile(String uid) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(uid);
        return documentReference.get().get().toObject(UserProfile.class);
    }
}
