package com.example.breken30daysback.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
public class ZohoAuthService {
    @Value("${zoho.client.id}")
    private String clientId;

    @Value("${zoho.client.secret}")
    private String clientSecret;

    @Value("${zoho.refresh.token}")
    private String refreshToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String tokenUrl = "https://accounts.zoho.com/oauth/v2/token";

    private String accessToken;
    private Instant tokenExpiryTime;

    /**
     * Fetch a new access token and store it with an expiry timestamp.
     */
    private void fetchNewAccessToken() {
        String url = tokenUrl + "?grant_type=refresh_token" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&refresh_token=" + refreshToken;

        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            this.accessToken = response.getBody().get("access_token").toString();
            int expiresIn = (int) response.getBody().get("expires_in"); // Expiry time in seconds
            this.tokenExpiryTime = Instant.now().plusSeconds(expiresIn - 60); // Buffer of 1 min
        } else {
            throw new RuntimeException("Failed to refresh Zoho access token.");
        }
    }

    /**
     * Get a valid access token (cached if still valid).
     */
    public String getAccessToken() {
        if (accessToken == null || tokenExpiryTime == null || Instant.now().isAfter(tokenExpiryTime)) {
            fetchNewAccessToken();
        }
        return accessToken;
    }
}
