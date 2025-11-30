package com.calendarize.calendarize.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calendarize.calendarize.Services.ProviderVerificationService;

@RestController
@RequestMapping("/api")
public class ProviderVerificationController {
    private final ProviderVerificationService providerVerificationService;

    public ProviderVerificationController(ProviderVerificationService providerVerificationService) {
        this.providerVerificationService = providerVerificationService;
    }

    @PostMapping("/verify-login")
    public ResponseEntity<Map<String, Object>> verifyLogin(@RequestBody Map<String, String> request) {
        String deviceId = request.get("device-id");
        String userId = request.get("user-id");
        String provider = request.get("provider");

        Map<String, Object> res = new HashMap<>();
        res.put("provider", provider);
        res.put("logged_in", true);
        if (!providerVerificationService.isValidLogin(deviceId, userId, provider)) {
            res.put("logged_in", false);
        }
        return ResponseEntity.ok(res);
    }
}
