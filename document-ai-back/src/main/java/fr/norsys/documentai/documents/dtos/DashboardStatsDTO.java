package fr.norsys.documentai.documents.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalDocuments;
    private long totalUsers;
    private long documentsPdf;
    private long documentsWord;
    private long documentsExcel;
    private long documentsRecent;
    private long documentsWithFeedback;
    private long documentsWithoutFeedback;


}