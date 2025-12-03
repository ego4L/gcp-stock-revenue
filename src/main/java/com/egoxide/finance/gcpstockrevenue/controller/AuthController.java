package com.egoxide.finance.gcpstockrevenue.controller;

import com.egoxide.finance.gcpstockrevenue.model.UserProfile;
import com.egoxide.finance.gcpstockrevenue.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint which frontend invokes after receiving ID Token from Firebase.
     * @param user Authenticated user (UID из FirebaseTokenFilter)
     * @param requestBody request body which contains email and name (optional)
     */
    @PostMapping("/login-success")
    public ResponseEntity<?> handleLoginAndRegistration(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody Map<String, String> requestBody) {

        String uid = user.getUsername();
        String email = requestBody.get("email");
        String name = requestBody.getOrDefault("name", "New User");

        try {

            UserProfile profile = userService.createOrUpdateUserProfile(uid, email, name);
            return ResponseEntity.ok(profile);

        } catch (ExecutionException | InterruptedException e) {

            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body("ERROR: Create/Update user profile.");
        }
    }
}
