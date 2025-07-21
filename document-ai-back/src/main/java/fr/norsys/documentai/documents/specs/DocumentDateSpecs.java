package fr.norsys.documentai.documents.specs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DocumentDateSpecs {

    public static Specification<Document> createdAtBetween(LocalDate start, LocalDate end) {
        return (root, query, builder) ->
            builder.between(root.get("createdAt"), start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    public static Specification<Document> updatedAtBetween(LocalDate start, LocalDate end) {
        return (root, query, builder) ->
            builder.between(root.get("updatedAt"), start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }
}
