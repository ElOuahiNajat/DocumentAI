package fr.norsys.documentai.documents.specs;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentFileSizeSpecs {
    public static Specification<Document> fileSizeGreaterThan(long size) {
        return (root, query, builder) -> builder.greaterThan(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeGreaterThanOrEqual(long size) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeLessThan(long size) {
        return (root, query, builder) -> builder.lessThan(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeLessThanOrEqual(long size) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("fileSize"), size);
    }

    public static Specification<Document> fileSizeEqual(long size) {
        return (root, query, builder) -> builder.equal(root.get("fileSize"), size);
    }
    
}
