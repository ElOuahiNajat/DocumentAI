package fr.norsys.documentai.documents.entitySpecs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentFileSizeSpecs {
    public static Specification<Document> fileSizeGreaterThan(int size) {
        return (root, query, builder) -> builder.greaterThan(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeGreaterThanOrEqual(int size) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeLessThan(int size) {
        return (root, query, builder) -> builder.lessThan(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeLessThanOrEqual(int size) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeEqual(int size) {
        return (root, query, builder) -> builder.equal(root.get("fileSize"), size);
    }
}
