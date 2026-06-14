// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/service/impl/ReportServiceImpl.java
package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.report.ReportRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Artwork;
import com.example.backend.model.Report;
import com.example.backend.model.User;
import com.example.backend.repository.ArtworkRepository;
import com.example.backend.repository.ReportRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ArtworkRepository artworkRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void submitReport(Long artworkId, ReportRequest req, Long userId) {
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException("Artwork not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Report report = Report.builder()
                .artwork(artwork)
                .user(user)
                .reason(req.getReason())
                .build();
        reportRepository.save(report);
    }

    @Override
    public List<?> getAllReports() {
        return reportRepository.findAll().stream().map(r -> new Object() {
            public Long id = r.getId();
            public String artworkTitle = r.getArtwork().getTitle();
            public String reporter = r.getUser().getUsername();
            public String reason = r.getReason();
        }).collect(Collectors.toList());
    }

    @Override
    public void dismissReport(Long reportId) {
        reportRepository.findById(reportId).orElseThrow(() -> new NotFoundException("Report not found"));
        reportRepository.deleteById(reportId);
    }

    @Override
    public void deleteArtworkByReport(Long reportId, Long artworkId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new NotFoundException("Report not found"));
        artworkRepository.deleteById(artworkId);
        reportRepository.deleteById(reportId);
    }
}
