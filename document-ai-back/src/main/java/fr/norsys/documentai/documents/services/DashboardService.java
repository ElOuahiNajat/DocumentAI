package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DashboardStatsDTO;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import fr.norsys.documentai.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DashboardStatsDTO getDashboardStats() {
        long totalDocuments = documentRepository.count();
        long totalUsers = userRepository.count();

        long documentsPdf = documentRepository.countByFileType("application/pdf");

        long documentsWord =
                documentRepository.countByFileType("application/msword") +
                        documentRepository.countByFileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        long documentsExcel =
                documentRepository.countByFileType("application/vnd.ms-excel") +
                        documentRepository.countByFileType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") +
                        documentRepository.countByFileType("text/csv");

        long documentsRecent = documentRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(7));

        long documentsWithFeedback = documentRepository.countDocumentsWithFeedback();
        long documentsWithoutFeedback = totalDocuments - documentsWithFeedback;

        return new DashboardStatsDTO(
                totalDocuments,
                totalUsers,
                documentsPdf,
                documentsWord,
                documentsExcel,
                documentsRecent,
                documentsWithFeedback,
                documentsWithoutFeedback

        );
    }
}
