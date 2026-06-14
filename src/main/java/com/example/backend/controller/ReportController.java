// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/controller/ReportController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.report.ReportRequest;
import com.example.backend.security.SecurityUtils;
import com.example.backend.service.ReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    @PostMapping("/artworks/{artworkId}/reports")
    public ResponseEntity<ApiResponse<Object>> submitReport(@PathVariable Long artworkId, @Valid @RequestBody ReportRequest req) {
        Long userId = getUserId();
        reportService.submitReport(artworkId, req, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(true, "Report submitted", null));
    }

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<?>>> getAllReports() {
        List<?> reports = reportService.getAllReports();
        return ResponseEntity.ok(ApiResponse.ok(reports));
    }

    @PostMapping("/admin/reports/{reportId}/dismiss")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> dismissReport(@PathVariable Long reportId) {
        reportService.dismissReport(reportId);
        return ResponseEntity.ok(ApiResponse.of(true, "Report dismissed", null));
    }

    @DeleteMapping("/admin/reports/{reportId}/artworks/{artworkId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteArtworkByReport(@PathVariable Long reportId, @PathVariable Long artworkId) {
        reportService.deleteArtworkByReport(reportId, artworkId);
        return ResponseEntity.ok(ApiResponse.of(true, "Artwork and report deleted", null));
    }
}
