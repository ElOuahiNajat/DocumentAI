package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalTime;

public class DocumentDateSpecs {
    public static Specification<Document> createdAtBetween(LocalDate start, LocalDate end) {
        return (root, query, builder) ->
            builder.between(root.get("createdAt"), start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public static Specification<Document> updatedAtBetween(LocalDate start, LocalDate end) {
        return (root, query, builder) ->
            builder.between(root.get("updatedAt"), start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }
}
