// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/ReportService.java
package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.report.ReportRequest;

public interface ReportService {
    void submitReport(Long artworkId, ReportRequest req, Long userId);
    List<?> getAllReports();
    void dismissReport(Long reportId);
    void deleteArtworkByReport(Long reportId, Long artworkId);
}
