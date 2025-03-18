package com.example.breken30daysback.Controllers;

import com.example.breken30daysback.Service.ZohoCRMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zoho")
public class ZohoCRMController {
    private final ZohoCRMService zohoCRMService;

    public ZohoCRMController(ZohoCRMService zohoCRMService) {
        this.zohoCRMService = zohoCRMService;
    }

    @PostMapping("/create-property")
    public ResponseEntity<String> createProperty(@RequestBody Map<String, Object> propertyData) {
        String response = zohoCRMService.createProperty(propertyData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sync-properties")
    public ResponseEntity<List<String>> syncProperties() {
        List<String> syncedProperties = zohoCRMService.syncPropertiesToZoho();
        return ResponseEntity.ok(syncedProperties);
    }

}
